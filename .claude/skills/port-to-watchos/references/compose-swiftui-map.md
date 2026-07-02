# Compose → SwiftUI mapping

## Layout & UI

| Compose | SwiftUI | Notes |
|---|---|---|
| `Column` / `Row` / `Box` | `VStack` / `HStack` / `ZStack` | Arrangement→`spacing`, Alignment→`alignment` |
| `Modifier.fillMaxSize()` | `.frame(maxWidth: .infinity, maxHeight: .infinity)` | |
| `Spacer(Modifier.height(n.dp))` | `Spacer().frame(height: n)` | dp→pt, same numeric value |
| `Text(fontSize=n.sp, fontWeight, lineHeight)` | `Text` + `.font(.system(size: n, weight:))` + `.lineSpacing` | sp→pt, same numeric value |
| `Image(painterResource(R.drawable.x))` | `Image("x")` | copy assets into Assets.xcassets |
| `AnimatedVisibility(fadeIn/fadeOut)` | `.transition(.opacity)` + `withAnimation` | keep durations identical |
| repeating animations (`rememberInfiniteTransition`) | `TimelineView` or `.repeatForever` | keep intervals identical |

## State & logic

| Compose/Kotlin | Swift | Notes |
|---|---|---|
| `remember { mutableStateOf }` | `@State` | |
| ViewModel + StateFlow (MVI) | `@Observable` class + State struct | keep the Intent enum; SideEffect as AsyncStream |
| `LaunchedEffect(key)` | `.task(id:)` | |
| `viewModelScope.launch` | `Task { }` | mind cancellation propagation |
| `Flow<T>` (callbackFlow) | `AsyncStream<T>` | callback bridge |
| `withTimeout(ms)` | timeout race via `withThrowingTaskGroup` | |

## Navigation

| Compose | SwiftUI |
|---|---|
| `NavHost` + `NavController` | `NavigationStack(path:)` + route enum |
| `navController.navigate(Route)` | `path.append(Route)` |
| navigation via SideEffect | subscribe to the model's AsyncStream, then mutate path |

## Platform services

| Wear OS | watchOS | Notes |
|---|---|---|
| Health Services `MeasureClient` (HEART_RATE_BPM) | HealthKit `HKWorkoutSession` + `HKLiveWorkoutBuilder` | real-time heart rate requires a workout session |
| accompanist permissions | `HKHealthStore.requestAuthorization` | Info.plist usage strings required |
| `SplashScreen` API | watchOS default launch screen | |
