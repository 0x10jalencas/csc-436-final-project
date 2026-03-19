package com.example.csc_436_final_project.ui.enneagram.compatibility

data class MatchResult(
    val scorePercentage: Int,
    val dynamicName: String,
    val strengths: String,
    val challenges: String
)

object CompatibilityEngine {

    // All 45 unique Enneagram pairings
    private val matchDatabase: Map<String, MatchResult> = mapOf(
        // TYPE 1 PAIRINGS
        "1-1" to MatchResult(80, "The Reformers", "Shared values, high ethical standards, and mutual respect for hard work.", "Can become overly critical of each other and forget to relax."),
        "1-2" to MatchResult(85, "The Caretakers", "The 1 provides structure, while the 2 provides warmth and emotional support.", "The 1 can feel overwhelmed by the 2's emotional needs; the 2 can feel unappreciated."),
        "1-3" to MatchResult(80, "The Achievers", "Highly productive, goal-oriented, and capable of building a secure life together.", "May focus too much on work and appearance, neglecting emotional connection."),
        "1-4" to MatchResult(75, "The Idealists", "Both share a deep sense of purpose and a desire to improve the world.", "The 1's need for order clashes with the 4's emotional volatility."),
        "1-5" to MatchResult(80, "The Intellectuals", "Objective, respectful, and highly analytical. They give each other space.", "Can become emotionally detached and overly serious."),
        "1-6" to MatchResult(85, "The Stabilizers", "Extremely loyal, responsible, and committed to doing the right thing.", "Both are prone to anxiety; the 1 worries about being wrong, the 6 about danger."),
        "1-7" to MatchResult(75, "The Paradox", "The 7 brings joy and spontaneity; the 1 brings grounding and follow-through.", "The 1 finds the 7 irresponsible; the 7 finds the 1 rigid and critical."),
        "1-8" to MatchResult(80, "The Trailblazers", "Action-oriented, passionate, and deeply committed to justice and truth.", "Power struggles. Both want control and believe they are objectively right."),
        "1-9" to MatchResult(90, "The Harmonizers", "The 9 softens the 1's perfectionism, while the 1 gives the 9 direction.", "The 1 gets frustrated with the 9's passivity; the 9 resents the 1's nagging."),

        // TYPE 2 PAIRINGS
        "2-2" to MatchResult(80, "The Nurturers", "Incredible warmth, mutual appreciation, and emotional generosity.", "Boundary issues. Both focus on the other and ignore their own needs."),
        "2-3" to MatchResult(85, "The Motivators", "Outgoing, socially adept, and highly supportive of each other's goals.", "Can become too focused on public image and what others think of them."),
        "2-4" to MatchResult(80, "The Romantics", "Deeply emotional, empathetic, and capable of profound intimacy.", "The 2 wants to fix the 4; the 4 pulls away feeling misunderstood."),
        "2-5" to MatchResult(70, "The Opposites", "The 2 brings the 5 out of their shell; the 5 grounds the 2's emotions.", "The 2 needs constant connection; the 5 needs extreme isolation."),
        "2-6" to MatchResult(85, "The Loyalists", "Deeply committed, protective, and focused on building a safe family unit.", "The 2 can feel the 6 is too pessimistic; the 6 doubts the 2's ulterior motives."),
        "2-7" to MatchResult(85, "The Entertainers", "Fun-loving, enthusiastic, and highly social. A very joyful pairing.", "Both avoid negative emotions. The 2 may feel unloved by the 7's flightiness."),
        "2-8" to MatchResult(90, "The Protectors", "The 8 provides strength and security; the 2 provides softness and care.", "The 8 can be overly blunt and hurt the 2; the 2 can be manipulative to get needs met."),
        "2-9" to MatchResult(85, "The Comfort Zone", "Peaceful, gentle, and extremely accommodating of each other.", "Passive-aggressiveness. Neither wants to bring up conflicts or negative feelings."),

        // TYPE 3 PAIRINGS
        "3-3" to MatchResult(80, "The Power Couple", "High energy, highly successful, and mutually understanding of ambition.", "Competitiveness. They may treat the relationship like a business transaction."),
        "3-4" to MatchResult(75, "The Image Makers", "The 3 brings practicality; the 4 brings depth and authentic creativity.", "The 3 hides flaws to look good; the 4 exposes flaws to be authentic. Major clash."),
        "3-5" to MatchResult(80, "The Strategists", "The 3 handles the execution; the 5 provides the deep research and ideas.", "The 3 wants to move fast and be seen; the 5 wants to observe from the shadows."),
        "3-6" to MatchResult(85, "The Builders", "The 3 provides ambition and drive; the 6 provides loyalty and risk-assessment.", "The 3 gets frustrated by the 6's hesitation; the 6 distrusts the 3's changing personas."),
        "3-7" to MatchResult(90, "The Visionaries", "Extremely dynamic, positive, and capable of building grand adventures.", "Neither wants to deal with sadness or deep emotional processing."),
        "3-8" to MatchResult(80, "The Empire Builders", "Unstoppable force. Highly assertive, confident, and action-oriented.", "Ego clashes. Both want to be the boss and hate showing vulnerability."),
        "3-9" to MatchResult(85, "The Balancers", "The 3 brings energy to the 9; the 9 brings peace and unconditional love to the 3.", "The 3 can view the 9 as lazy; the 9 can feel used or left behind by the 3."),

        // TYPE 4 PAIRINGS
        "4-4" to MatchResult(75, "The Deep Divers", "Profound understanding of each other's emotional depths and artistic needs.", "Prone to dramatic spirals, melancholy, and a lack of practical grounding."),
        "4-5" to MatchResult(85, "The Observers", "Both are introverted, intense, and love deep, meaningful conversations.", "The 4 wants emotional fusion; the 5 wants emotional distance."),
        "4-6" to MatchResult(80, "The Truth Seekers", "Both are reactive and authentic. The 6 offers loyalty; the 4 offers emotional depth.", "The 6 needs certainty; the 4 is constantly shifting emotionally. Leads to anxiety."),
        "4-7" to MatchResult(75, "The Highs & Lows", "The 7 brings optimism and fun; the 4 brings depth and meaning.", "The 7 runs from pain; the 4 wallows in it. Fundamentally different coping mechanisms."),
        "4-8" to MatchResult(80, "The Intensities", "Both are passionate and refuse to be controlled. Highly magnetic.", "Explosive arguments. The 8 demands action; the 4 demands emotional validation."),
        "4-9" to MatchResult(85, "The Dreamers", "Gentle, imaginative, and highly accepting of each other's quirks.", "The 4 wants intense emotional reactions; the 9 numbs out to avoid intensity."),

        // TYPE 5 PAIRINGS
        "5-5" to MatchResult(85, "The Fortress", "Utter respect for boundaries, privacy, and intellectual pursuits.", "Can become so isolated that the relationship essentially fades into a roommate dynamic."),
        "5-6" to MatchResult(80, "The Analysts", "Highly intellectual, practical, and focused on understanding the world.", "Paranoia and overthinking. Both can get stuck in their heads and forget to act."),
        "5-7" to MatchResult(80, "The Innovators", "The 5 provides deep focus; the 7 provides expansive brainstorming and energy.", "The 7 demands too much energy; the 5 retreats to recharge, causing a chase dynamic."),
        "5-8" to MatchResult(85, "The Independent Forces", "Both respect strength and independence. The 8 acts; the 5 strategizes.", "The 8 pushes for conflict to connect; the 5 withdraws completely from conflict."),
        "5-9" to MatchResult(85, "The Quiet Retreat", "Peaceful, undemanding, and comfortable in companionable silence.", "Lethargy. Neither naturally initiates action or deals with emotional maintenance."),

        // TYPE 6 PAIRINGS
        "6-6" to MatchResult(80, "The Loyal Guardians", "Unmatched loyalty, commitment, and mutual support in a crisis.", "Echo chamber of anxiety. They can feed each other's worst-case scenarios."),
        "6-7" to MatchResult(80, "The Anchor & Balloon", "The 6 brings safety; the 7 brings joy and pulls the 6 into new experiences.", "The 6 feels the 7 is reckless; the 7 feels the 6 is a buzzkill."),
        "6-8" to MatchResult(85, "The Defenders", "Intensely loyal and protective of their inner circle. They face the world together.", "Trust issues. The 6 questions the 8's authority; the 8 hates the 6's second-guessing."),
        "6-9" to MatchResult(90, "The Safe Haven", "Steady, predictable, and deeply comforting. They provide exactly what the other needs.", "Can get stuck in a rut and avoid dealing with necessary external conflicts."),

        // TYPE 7 PAIRINGS
        "7-7" to MatchResult(80, "The Endless Adventure", "Constant fun, high energy, and a mutual hatred of feeling trapped.", "Avoidance of reality. Neither wants to do the boring chores or pay the bills."),
        "7-8" to MatchResult(85, "The Mavericks", "High energy, fearless, and totally unapologetic about going after what they want.", "Impulsivity. They can burn through resources and escalate conflicts quickly."),
        "7-9" to MatchResult(90, "The Optimists", "A breezy, positive pairing. The 7 brings the fun; the 9 goes with the flow.", "The 7 can walk all over the 9's unspoken boundaries; the 9 responds with stubbornness."),

        // TYPE 8 PAIRINGS
        "8-8" to MatchResult(80, "The Titans", "Incredible power, passion, and mutual respect for strength.", "Constant power struggles. Neither knows how to back down or be vulnerable."),
        "8-9" to MatchResult(90, "The Bear & The Cub", "The 8 protects and energizes the 9; the 9 calms and accepts the 8.", "The 8 can steamroll the 9. The 9's passive-resistance will eventually enrage the 8."),

        // TYPE 9 PAIRINGS
        "9-9" to MatchResult(85, "The Sanctuary", "Total acceptance, peace, and an incredibly harmonious home life.", "Total inertia. Neither wants to make a decision or rock the boat to fix problems.")
    )

    fun calculateMatch(userCore: Int, partnerCore: Int): MatchResult {
        // Ensure valid inputs
        if (userCore !in 1..9 || partnerCore !in 1..9) {
            return MatchResult(0, "Unknown", "Data missing.", "Please complete the test.")
        }

        // Sort them so the order doesn't matter (1 & 7 is the same as 7 & 1)
        val pairing = if (userCore <= partnerCore) "$userCore-$partnerCore" else "$partnerCore-$userCore"

        return matchDatabase[pairing] ?: MatchResult(
            scorePercentage = 75,
            dynamicName = "Unique Connection",
            strengths = "Both types bring unique perspectives to the table.",
            challenges = "Requires active communication to understand differences."
        )
    }
}