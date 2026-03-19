# Persona Enneagram Personality App

Author: Jess A. Alencaster  
Class: CSC 436: Mobile App Development  
Professor Ken Kubiak

### Project Description
Persona is a sampled RHETI®-based Enneagram personality test app designed to
help users easily discover their Enneagram type. Additionally, they can then
find out their Enneagram compatibility between their own Enneagram type and the
individual they sent the link to (or manually insert a different Enneagram
type).

The app features a full implementation of the RHETI® (Riso-Hudson Enneagram Type
Indicator) via a sampling test. Instead of the official full-fledged RHETI®
personality test that is roughly 116 questions in size, this sampled version
features 36 questions, which calculates both a user's primary type and their
"wing."

The app then includes a "Compatibility Engine" that provides detailed strengths
and challenges for every unique type pairing. To elevate this experience for
interested users of this feature, an AI-powered "Relationship Coach" integrated
via Google Gemini provides personalized, real-time advice for couples. The app
finally has deep-linking capabilities, allowing users to share their results and
instantly check compatibility with friends uppon clicking the link.

### Figma Design
*You can view the design assets and wireframes below:*
- **Figma Project Link:** [View
  Wireframes](https://www.figma.com/design/hvXdBWCOo6bHz0heCRGf3x/CSC-436-Wireframes?node-id=2003-446&t=ODhCaa788h3vXTfl-0)

### Key Features & Technical Implementation
Here is the list of the Android and Jetpack Compose features I utilized:
- **Jetpack Compose:** The entire UI is built using a reactive, declarative
  approach with Material 3 components.
- **Compose Navigation (Type-Safe):** Utilizes the latest Kotlin
  Serialization-based navigation to pass complex data between screens securely.
- **Google Generative AI (Gemini):** Integrated the `generativeai` SDK to
  provide dynamic, AI-driven relationship "Pro-Tips" based on specific Enneagram
  pairings.
- **DataStore Preferences:** For a persistence layer to save test progress
  (allowing users to resume where they left off) and store the final result
  across app sessions.
- **Deep Linking:** Configured a custom URI scheme (`enneagram://match`) that
  allows users to share a link containing their type. When clicked, the app
  opens directly to the compatibility screen with their type pre-filled.
- **ViewModel & StateFlow:** Maintains a clean architecture by separating
  business logic (test scoring, state management) from the UI layer.

### Technical Requirements
- **Minimum SDK:** API 24 (Android 7.0)
- **Target SDK:** API 34
- **Internet Permission:** Required for AI Coach insights via the Gemini API.
  Enabled internet via AndriodManifest.xml
- **Dependencies:**
  - `androidx.navigation:navigation-compose:2.8.5`
  - `androidx.datastore:datastore-preferences:1.1.1`
  - `com.google.ai.client.generativeai:generativeai:0.9.0`
  - `org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1`

### Additional Notes:
- **AI Integration:** Could've left off the compatibility matching at just
static text, but with the new hype around GenAI, I experimented (for the first
time) with calling a live GenAI model (Gemini's free versoin) to give unique,
contextual relationship advice.
- **Deep Link Sharing:** Could've left it off at sharing just text between two
users, but instead I figured out how to implement a custom Intent Filter that
  handles deep links, making the app "social" by allowing users to send their
  results to others via a clickable link.
- **Sophisticated Scoring Logic:** The RHETI test implementation includes
  complex "wing" calculation logic based on adjacent types. Some RHETI®-based
  apps do not provide this "wing" calculation.
- **Resumable State:** Taking some notes from the importance of data
  persistence, I ensured that the `answersEncoded` string in DataStore, in the
  app handles process death or manual exits gracefully, never losing the user's
  progress during the 36-question test.
