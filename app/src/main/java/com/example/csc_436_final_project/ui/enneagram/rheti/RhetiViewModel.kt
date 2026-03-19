

package com.example.csc_436_final_project.ui.enneagram.rheti

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

enum class RhetiChoice { A, B }
enum class Column { A, B, C, D, E, F, G, H, I }

data class RhetiStatement(
    val text: String,
    val column: Column
)

data class RhetiQuestion(
    val optionA: RhetiStatement, // A box
    val optionB: RhetiStatement  // B box
)

data class RhetiUiState(
    val questions: List<RhetiQuestion>,
    val index: Int,
    val answers: List<RhetiChoice?> // null = unanswered
) {
    val total: Int get() = questions.size
    val isLast: Boolean get() = index == questions.lastIndex
    val current: RhetiQuestion get() = questions[index]
    val currentAnswer: RhetiChoice? get() = answers[index]
}

class RhetiViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = RhetiPreferencesRepository(application.applicationContext)

    // Official sampler key (mapping)
    private val columnToType: Map<Column, Int> = mapOf(
        Column.A to 9,
        Column.B to 6,
        Column.C to 3,
        Column.D to 1,
        Column.E to 4,
        Column.F to 2,
        Column.G to 8,
        Column.H to 5,
        Column.I to 7
    )

    // Wing adjacency
    private val typeNeighbors: Map<Int, Pair<Int, Int>> = mapOf(
        1 to (9 to 2),
        2 to (1 to 3),
        3 to (2 to 4),
        4 to (3 to 5),
        5 to (4 to 6),
        6 to (5 to 7),
        7 to (6 to 8),
        8 to (7 to 9),
        9 to (8 to 1)
    )

    private fun q(
        aText: String, aCol: Column,
        bText: String, bCol: Column
    ) = RhetiQuestion(
        optionA = RhetiStatement(aText, aCol),
        optionB = RhetiStatement(bText, bCol)
    )

    /**
     * 36 questions with column mapping
     * from official network codes that I did
     * literature review on.
     */
    private val samplerQuestions: List<RhetiQuestion> = listOf(
        q("I've been romantic and imaginative.", Column.E, "I've been pragmatic and down to earth.", Column.B), // EB143
        q("I have tended to take on confrontations.", Column.G, "I have tended to avoid confrontations.", Column.A), // GA142
        q("I have typically been diplomatic, charming, and ambitious.", Column.C, "I have typically been direct, formal, and idealistic.", Column.D), // CD141
        q("I have tended to be focused and intense.", Column.H, "I have tended to be spontaneous and fun-loving.", Column.I), // HI140
        q("I have been a hospitable person and have enjoyed welcoming new friends into my life.", Column.F, "I have been a private person and have not mixed much with others.", Column.E), // FE139
        q("Generally, it's been easy to \"get a rise\" out of me.", Column.B, "Generally, it's been difficult to \"get a rise\" out of me.", Column.A), // BA138
        q("I've been more of a \"street-smart\" survivor.", Column.G, "I've been more of a \"high-minded\" idealist.", Column.D), // GD137
        q("I have needed to show affection to people.", Column.F, "I have preferred to maintain a certain distance with people.", Column.H), // FH136
        q("When presented with a new experience, I've usually asked myself if it would be useful to me.", Column.C, "When presented with a new experience, I've usually asked myself if it would be enjoyable.", Column.I), // CI135
        q("I have tended to focus too much on myself.", Column.E, "I have tended to focus too much on others.", Column.A), // EA134
        q("Others have depended on my insight and knowledge.", Column.H, "Others have depended on my strength and decisiveness.", Column.G), // HG133
        q("I have come across as being too unsure of myself.", Column.B, "I have come across as being too sure of myself.", Column.D), // BD132
        q("I have been more relationship-oriented than goal-oriented.", Column.F, "I have been more goal-oriented than relationship-oriented.", Column.C), // FC131
        q("I have not been able to speak up for myself very well.", Column.E, "I have been outspoken—I've said what others wished they had the nerve to say.", Column.I), // EI130
        q("It's been difficult for me to stop considering alternatives and do something definite.", Column.H, "It's been difficult for me to take it easy and be more flexible.", Column.D), // HD129
        q("I have tended to be hesitant and procrastinating.", Column.B, "I have tended to be bold and domineering.", Column.G), // BG128
        q("My reluctance to get too involved has gotten me into trouble with people.", Column.A, "My eagerness to have people depend on me has gotten me into trouble with them.", Column.F), // AF127
        q("Usually, I have been able to put my feelings aside to get the job done.", Column.C, "Usually, I have needed to work through my feelings before I could act.", Column.E), // CE126
        q("Generally, I have been methodical and cautious.", Column.B, "Generally, I have been adventurous and taken risks.", Column.I), // BI125
        q("I have tended to be a supportive, giving person who enjoys the company of others.", Column.F, "I have tended to be a serious, reserved person who likes discussing issues.", Column.D), // FD124
        q("I've often felt the need to be a \"pillar of strength.\"", Column.G, "I've often felt the need to perform perfectly.", Column.C), // GC123
        q("I've typically been interested in asking tough questions and maintaining my independence.", Column.H, "I've typically been interested in maintaining my stability and peace of mind.", Column.A), // HA122
        q("I've been too hard-nosed and skeptical.", Column.B, "I've been too soft-hearted and sentimental.", Column.F), // BF121
        q("I've often worried that I'm missing out on something better.", Column.I, "I've often worried that if I let down my guard, someone will take advantage of me.", Column.G), // IG120
        q("My habit of being \"stand-offish\" has annoyed people.", Column.E, "My habit of telling people what to do has annoyed people.", Column.D), // ED119
        q("Usually, when troubles have gotten to me, I have been able to \"tune them out.\"", Column.A, "Usually, when troubles have gotten to me, I have treated myself to something I've enjoyed.", Column.I), // AI118
        q("I have depended upon my friends and they have known that they can depend on me.", Column.B, "I have not depended on people; I have done things on my own.", Column.C), // BC117
        q("I have tended to be detached and preoccupied.", Column.H, "I have tended to be moody and self-absorbed.", Column.E), // HE116
        q("I have liked to challenge people and \"shake them up.\"", Column.G, "I have liked to comfort people and calm them down.", Column.F), // GF115
        q("I have generally been an outgoing, sociable person.", Column.I, "I have generally been an earnest, self-disciplined person.", Column.D), // ID114
        q("I've usually been shy about showing my abilities.", Column.A, "I've usually liked to let people know what I can do well.", Column.C), // AC113
        q("Pursuing my personal interests has been more important to me than having comfort and security.", Column.H, "Having comfort and security has been more important to me than pursuing my personal interests.", Column.B), // HB112
        q("When I've had conflict with others, I've tended to withdraw.", Column.E, "When I've had conflict with others, I've rarely backed down.", Column.G), // EG111
        q("I have given in too easily and let others push me around.", Column.A, "I have been too uncompromising and demanding with others.", Column.D), // AD110
        q("I've been appreciated for my unsinkable spirit and great sense of humor.", Column.I, "I've been appreciated for my quiet strength and exceptional generosity.", Column.F), // IF109
        q("Much of my success has been due to my talent for making a favorable impression.", Column.C, "Much of my success has been achieved despite my lack of interest in developing \"interpersonal skills.\"", Column.H) // CH108
    )

    // in memory states for UI updates (live)
    private val _uiState = MutableStateFlow(
        RhetiUiState(
            questions = samplerQuestions,
            index = 0,
            answers = List(samplerQuestions.size) { null }
        )
    )
    val uiState: StateFlow<RhetiUiState> = _uiState.asStateFlow()

    init {
        // Restore persisted progress
        viewModelScope.launch {
            val saved = repo.persistedFlow.first()
            val total = samplerQuestions.size

            val restoredAnswers = decodeAnswers(saved.answersEncoded, total)
            val restoredIndex = saved.index.coerceIn(0, total - 1)

            _uiState.value = _uiState.value.copy(
                index = restoredIndex,
                answers = restoredAnswers
            )
        }
    }

    fun reset() {
        _uiState.value = RhetiUiState(
            questions = samplerQuestions,
            index = 0,
            answers = List(samplerQuestions.size) { null }
        )
        viewModelScope.launch {
            repo.clearProgressOnly()
        }
    }

    /**
     * Records choice and auto-advances unless last.
     * Returns true if finished after choosing.
     */
    fun choose(choice: RhetiChoice): Boolean {
        val s = _uiState.value
        val newAnswers = s.answers.toMutableList()
        newAnswers[s.index] = choice

        val finished = s.isLast
        _uiState.value = if (!finished) {
            s.copy(answers = newAnswers, index = s.index + 1)
        } else {
            s.copy(answers = newAnswers)
        }

        // Persist after updating state
        persistProgressAndMaybeResult()

        return finished
    }

    fun prev() {
        val s = _uiState.value
        if (s.index > 0) {
            _uiState.value = s.copy(index = s.index - 1)
            persistProgressAndMaybeResult()
        }
    }

    fun answeredCount(): Int = _uiState.value.answers.count { it != null }

    fun scoresByColumn(): Map<Column, Int> {
        val s = _uiState.value
        val counts = Column.values().associateWith { 0 }.toMutableMap()

        s.questions.forEachIndexed { i, question ->
            when (s.answers[i]) {
                RhetiChoice.A -> counts[question.optionA.column] = counts.getValue(question.optionA.column) + 1
                RhetiChoice.B -> counts[question.optionB.column] = counts.getValue(question.optionB.column) + 1
                null -> Unit
            }
        }
        return counts
    }

    fun scoresByType(): Map<Int, Int> {
        val colCounts = scoresByColumn()
        val typeCounts = (1..9).associateWith { 0 }.toMutableMap()

        colCounts.forEach { (col, count) ->
            val type = columnToType.getValue(col)
            typeCounts[type] = typeCounts.getValue(type) + count
        }
        return typeCounts
    }

    fun primaryType(): Int {
        val scores = scoresByType()
        val max = scores.values.maxOrNull() ?: 0
        // tie-break: lowest type number
        return scores.filterValues { it == max }.keys.minOrNull() ?: 9
    }

    fun wingFor(primary: Int): Int? {
        val neighbors = typeNeighbors[primary] ?: return null
        val (n1, n2) = neighbors
        val scores = scoresByType()
        val s1 = scores[n1] ?: 0
        val s2 = scores[n2] ?: 0

        return when {
            s1 == s2 -> null
            s1 > s2 -> n1
            else -> n2
        }
    }

    // Note to self: INSIDE RhetiViewModel class

    data class TypeInfo(
        val title: String,        // for example, the "PEACEMAKER"
        val description: String   // then the para shown on results card
    )

    fun typeInfo(type: Int): TypeInfo {
        return when (type) {
            9 -> TypeInfo(
                title = "PEACEMAKER",
                description = "You value harmony, peace, and unity. You're naturally accepting, trusting, and stable, with a desire to keep the peace and avoid conflict."
            )
            1 -> TypeInfo("REFORMER", "Principled, purposeful, and self-controlled. You value doing what is right and improving things.")
            2 -> TypeInfo("HELPER", "Warm, caring, and supportive. You focus on connection and being helpful to others.")
            3 -> TypeInfo("ACHIEVER", "Driven and adaptable. You value success, accomplishment, and being effective.")
            4 -> TypeInfo("INDIVIDUALIST", "Sensitive and expressive. You value authenticity, meaning, and identity.")
            5 -> TypeInfo("INVESTIGATOR", "Perceptive and innovative. You value knowledge, understanding, and independence.")
            6 -> TypeInfo("LOYALIST", "Committed and security-oriented. You value trust, preparedness, and support.")
            7 -> TypeInfo("ENTHUSIAST", "Spontaneous and optimistic. You value freedom, variety, and new experiences.")
            8 -> TypeInfo("CHALLENGER", "Assertive and protective. You value strength, control, and justice.")
            else -> TypeInfo("TYPE $type", "Description coming soon.")
        }
    }

    private fun persistProgressAndMaybeResult() {
        val state = _uiState.value
        val encoded = encodeAnswers(state.answers)
        val completed = state.answers.all { it != null }

        viewModelScope.launch {
            repo.saveProgress(
                answersEncoded = encoded,
                index = state.index,
                completed = completed
            )

            if (completed) {
                val p = primaryType()
                val w = wingFor(p)
                repo.saveLastResult(
                    primaryType = p,
                    wing = w,
                    timestampMs = System.currentTimeMillis()
                )
            }
        }
    }
}

// --- DataStore encoding helpers (kept file-private) ---

private fun encodeAnswers(answers: List<RhetiChoice?>): String =
    buildString(answers.size) {
        answers.forEach {
            append(
                when (it) {
                    RhetiChoice.A -> 'A'
                    RhetiChoice.B -> 'B'
                    null -> '_'
                }
            )
        }
    }

private fun decodeAnswers(encoded: String, total: Int): List<RhetiChoice?> =
    List(total) { i ->
        when (encoded.getOrNull(i)) {
            'A' -> RhetiChoice.A
            'B' -> RhetiChoice.B
            else -> null
        }
    }