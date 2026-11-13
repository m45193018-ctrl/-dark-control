# DARK CONTROL — V1.0 Starter

A consent-first Android device management prototype.

## What's included
- Kotlin + Jetpack Compose Android app starter.
- Dark navy / black UI with neon-blue accents.
- Controller / managed-device mode selection.
- Pairing-code creation and entry UI (local demo flow only).
- Device dashboard with clearly marked sample status values.
- Consent screen and visible stop control concept.
- Prank Mode as an explicitly labeled harmless simulation.

## Safety and scope
This starter does **not** implement stealth access, permission bypass, covert recording,
hidden persistence, or unauthorized remote control. Status values and pairing are demo-only.
Real screen sharing, contacts sharing, media sharing, and remote interaction must be
implemented only with Android's visible permission and consent flows.

## Open in Android Studio
1. Install a recent Android Studio version with Android SDK.
2. Choose **Open** and select this folder.
3. Let Gradle sync.
4. Run on an Android device or emulator.

The project uses the standard Android Gradle Plugin and Kotlin plugin declarations.
If your Android Studio installation proposes a compatible Gradle/SDK update, accept it.

## Next milestones
1. Replace demo pairing with authenticated pairing and a secure backend.
2. Add device-to-device status exchange with explicit user approval.
3. Add Android MediaProjection screen sharing with a persistent visible notification
   and a stop action controlled by the sharing device.
4. Add user-selected media sharing through Android's system photo picker.
5. Add a separate Windows controller only after the Android consent workflow is stable.
