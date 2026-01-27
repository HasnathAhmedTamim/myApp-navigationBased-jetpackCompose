# MyApp - Complete Technical Documentation

## 📱 Project Overview
A modern Android application built with **Jetpack Compose** and **Material Design 3**, featuring authentication, navigation, and a multi-screen user interface.

---

## 🏗️ Architecture

### **Tech Stack**
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Navigation:** Type-safe Compose Navigation
- **Design System:** Material Design 3
- **Build Tool:** Gradle

### **Architecture Pattern**
- **Unidirectional Data Flow (UDF)**
    - Data flows DOWN: NavGraph → Screens (parameters)
    - Events flow UP: User actions → Callbacks → NavGraph
- **Single Source of Truth**: `NavGraph` holds app state
- **Separation of Concerns**: Screens are pure UI, navigation logic lives in `NavGraph`

---

## 📂 Project Structure

```
com.example.myapp/
├── MainActivity.kt          # Entry point, hosts Compose UI
├── navigation/
│   ├── NavGraph.kt         # Navigation orchestrator & state management
│   └── Screen.kt           # Type-safe route definitions
├── screens/
│   ├── LoginScreen.kt      # Authentication screen
│   ├── HomeScreen.kt       # Main dashboard with 4 cards
│   ├── ProfileScreen.kt    # User profile display
│   ├── SearchScreen.kt     # Search functionality
│   ├── CardDetailScreen.kt # Individual card details
│   ├── AlbumScreen.kt      # Album list view
│   ├── AlbumDetailScreen.kt    # Album overview
│   └── AlbumItemDetailScreen.kt # Individual album item
└── models/
    ├── User.kt            # User data model
    └── AlbumItem.kt       # Album data model
```

---

## 🔄 Complete Data Flow

### **1. App Startup**
```
MainActivity.onCreate()↓
setContent { NavGraph() }
    ↓
Creates navController (navigation manager)
Creates currentUser state (logged-in user)
Creates albumItems list (sample data)
    ↓
NavHost starts at Screen.Login
```

---

### **2. Login Flow**

#### **User Actions → State Updates → Navigation**
```
User types username/password
    ↓
TextField updates mutableStateOf (triggers recomposition)
    ↓
User taps "Login"
    ↓
LoginScreen calls onLoginSuccess(username, password)
    ↓
NavGraph receives callback:
  1. currentUser.value = User(...) // Store user data
  2. navigate(Screen.Home) {
       popUpTo<Screen.Login> { inclusive = true }
     }
    ↓
Back stack: [Login] → [Home]
    ↓
HomeScreen displays with user data
```

**Key Concepts:**
- `remember { mutableStateOf() }` keeps state across recompositions
- `popUpTo` clears login from back stack (prevents back to login)
- `inclusive = true` removes the target screen too

---

### **3. Home Screen Interactions**

#### **Card Click Example (Card 3)**
```
User taps Card 3
    ↓
Card's clickable { onCardClick("3", "Card 3") }
    ↓
NavGraph receives: cardId="3", cardTitle="Card 3"
    ↓
navController.navigate(Screen.CardDetail("3", "Card 3"))
    ↓
Navigation serializes: "cardDetail/3/Card%203"
    ↓
Adds to back stack: [Home] → [CardDetail]
    ↓
Destination extracts args via toRoute<Screen.CardDetail>()
    ↓
CardDetailScreen(cardId="3", cardTitle="Card 3")
    ↓
UI displays: TopBar("Card 3"), Text("Card ID: 3")
```

#### **Bottom Navigation**
```
User taps Search icon
    ↓
onSearchClick() callback
    ↓
navController.navigate(Screen.Search) {
  popUpTo<Screen.Home>() // Keep Home, remove others
}
    ↓
Back stack: [Home] → [Search]
```

---

### **4. Navigation Patterns**

#### **Pattern 1: Simple Navigation (Profile)**
```kotlin
// From HomeScreen
onProfileClick()
    ↓
navController.navigate(Screen.Profile)
    ↓
Stack: [Home] → [Profile]
```

#### **Pattern 2: Bottom Tab Navigation**
```kotlin
// From Search → Album
navigate(Screen.Album) {
  popUpTo<Screen.Home>() // Clear everything above Home
}
    ↓
Stack: [Home] → [Album]
```

#### **Pattern 3: Nested Detail Navigation**
```kotlin
// AlbumScreen → AlbumDetailScreen → AlbumItemDetailScreen
1. Album list click → Screen.AlbumDetail
2. Album detail item click → Screen.AlbumItemDetail(id, title)
3. Back navigation pops one level at a time
```

---

### **5. Type-Safe Navigation**

#### **Route Definitions (`Screen.kt`)**
```kotlin
@Serializable
sealed interface Screen {
    @Serializable data object Login
    @Serializable data object Home
    @Serializable data class CardDetail(
        val cardId: String,
        val cardTitle: String
    )
}
```

#### **Parameter Extraction**
```kotlin
// In NavGraph destination
composable<Screen.CardDetail> { backStackEntry ->
    val args = backStackEntry.toRoute<Screen.CardDetail>()
    CardDetailScreen(
        cardId = args.cardId,    // Type-safe access
        cardTitle = args.cardTitle
    )
}
```

**Benefits:**
- Compile-time type checking
- No string-based route bugs
- IDE autocomplete support
- Refactoring-friendly

---

### **6. Back Navigation Flow**

```
User presses back/back arrow
    ↓
onClick = onBack // Screen calls callback
    ↓
NavGraph's { navController.popBackStack() }
    ↓
Navigation removes top screen from stack
    ↓
Previous screen reappears (already in memory)
```

**Example:**
```
[Home] → [CardDetail] → [Back pressed]
    ↓
[Home] (CardDetail destroyed)
```

---

## 🎨 UI Components Breakdown

### **Scaffold Pattern** (Used in all screens)
```kotlin
Scaffold(
    topBar = { TopAppBar(...) },      // Title + actions
    bottomBar = { NavigationBar(...) } // Optional tabs
) { paddingValues ->
    // Content area (avoids overlap with bars)
}
```

### **LazyVerticalGrid** (HomeScreen cards)
```kotlin
LazyVerticalGrid(
    columns = GridCells.Fixed(2),  // 2 columns
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    items(4) { index ->
        Card(...) { Text("Card ${index + 1}") }
    }
}
```
- Efficient rendering (only visible items)
- Automatic spacing/alignment
- Lazy loading for large datasets

### **State Management** (SearchScreen example)
```kotlin
var searchQuery by remember { mutableStateOf("") }

TextField(
    value = searchQuery,
    onValueChange = { searchQuery = it } // Updates state
)
```
- `remember` survives recompositions
- `mutableStateOf` triggers UI updates
- Local state destroyed when screen leaves composition

---

## 🔐 Core Concepts Explained

### **1. Composable Functions**
```kotlin
@Composable
fun MyScreen() { }
```
- Functions that **describe UI** (not build it)
- Can call other `@Composable` functions
- Rerun automatically when state changes

---

### **2. State vs. Parameters**

| Type | Where | Lifecycle | Example |
|------|-------|-----------|---------|
| **State** | Inside screen | While in composition | `remember { mutableStateOf() }` |
| **Parameters** | From parent | Passed at creation | `username: String` |

**State triggers recomposition:**
```kotlin
var count by remember { mutableStateOf(0) }
Button(onClick = { count++ }) // UI updates automatically
```

---

### **3. Unidirectional Data Flow (UDF)**

```
┌───────────────────────────────────────┐
│         NavGraph (State Owner)        │
│  • currentUser: MutableState<User?>   │
│  • albumItems: List<AlbumItem>        │
└─────────────┬─────────────────────────┘
              │ Parameters ↓
              │┌─────────▼─────────┐
    │   Screen (UI)     │
    │  • Displays data  │
    │  • Reports events │
    └─────────┬─────────┘
              │ Callbacks ↑
              │
┌─────────────▼─────────────────────────┐
│      NavGraph (Event Handler)         │
│  • Updates state                      │
│  • Triggers navigation                │
└───────────────────────────────────────┘
```

**Example:**
```kotlin
// Data flows DOWN
HomeScreen(username = currentUser.value?.username ?: "User")

// Events flow UP
HomeScreen(
    onCardClick = { id, title ->
        navController.navigate(Screen.CardDetail(id, title))
    }
)
```

---

### **4. remember & Lifecycle**

```kotlin
val navController = rememberNavController()
```
- **Without `remember`**: recreated every recomposition → crashes
- **With `remember`**: survives recomposition, destroyed when leaves composition

---

### **5. Back Stack Management**

#### **Basic Navigation**
```kotlin
navigate(Screen.Profile)  // Push
popBackStack()           // Pop
```

#### **Advanced: popUpTo**
```kotlin
navigate(Screen.Home) {
    popUpTo<Screen.Login> { inclusive = true }
}
```
- **Before:** `[Login, Home]`
- **After:** `[Home]` (Login cleared)

**Use cases:**
- Login success: Clear login screen
- Bottom nav: Maintain single instance of each tab

---

## 🧪 Screen-by-Screen Details

### **LoginScreen**
- **State:** `username`, `password` (local)
- **Events:** `onLoginSuccess(username, password)`
- **Navigation Out:** → Home (clears login from stack)

### **HomeScreen**
- **Receives:** `username` from `currentUser`
- **Events:** 5 callbacks (profile, home, search, album, card clicks)
- **UI:** 4-card grid + top/bottom bars
- **Navigation:** Pushes ProfileScreen, CardDetailScreen, SearchScreen, AlbumScreen

### **CardDetailScreen**
- **Receives:** `cardId`, `cardTitle` (from route args)
- **Events:** `onBack`
- **UI:** Displays card info
- **Navigation:** Pops back to Home

### **ProfileScreen**
- **Receives:** `User?` object
- **Events:** `onBack`
- **UI:** Shows user details or "N/A"

### **SearchScreen**
- **State:** `searchQuery` (local)
- **Events:** Back, home, album navigation
- **UI:** Search bar (non-functional placeholder)

### **AlbumScreen**
- **Receives:** `albumItems` list
- **Events:** Item clicks, detail button, back/nav
- **UI:** Scrollable list of album cards
- **Navigation:** → AlbumDetailScreen, AlbumItemDetailScreen

### **AlbumDetailScreen**
- **Receives:** Full `albumItems` list
- **Events:** Item clicks, back
- **UI:** Grid of all albums

### **AlbumItemDetailScreen**
- **Receives:** `itemId`, `itemTitle` (route args) → looks up full item
- **Events:** `onBack`
- **UI:** Displays selected album details or "Not found"

---

## 🔧 How Functions Work Together

### **Example: Card Click → Detail Screen**

#### **1. HomeScreen.kt**
```kotlin
Card(modifier = Modifier.clickable {
    onCardClick("3", "Card 3")  // Reports event UP
})
```

#### **2. NavGraph.kt (HomeScreen definition)**
```kotlin
HomeScreen(
    onCardClick = { cardId, cardTitle ->
        navController.navigate(
            Screen.CardDetail(cardId, cardTitle)  // Handles event
        )
    }
)
```

#### **3. NavGraph.kt (CardDetail destination)**
```kotlin
composable<Screen.CardDetail> { backStackEntry ->
    val args = backStackEntry.toRoute<Screen.CardDetail>()
    CardDetailScreen(
        cardId = args.cardId,        // Passes data DOWN
        cardTitle = args.cardTitle,
        onBack = { navController.popBackStack() }
    )
}
```

#### **4. CardDetailScreen.kt**
```kotlin
@Composable
fun CardDetailScreen(
    cardId: String,      // Receives data
    cardTitle: String,
    onBack: () -> Unit
) {
    TopAppBar(title = { Text(cardTitle) })  // Uses data
    // ...
    IconButton(onClick = onBack)  // Reports back event UP
}
```

---

## 📊 Data Models

### **User.kt**
```kotlin
data class User(
    val username: String,
    val email: String
)
```
- Stored in `currentUser: MutableState<User?>`
- Updated on login success
- Passed to ProfileScreen

### **AlbumItem.kt**
```kotlin
data class AlbumItem(
    val id: String,
    val title: String,
    val description: String
)
```
- Static list in `NavGraph`
- Passed to album-related screens
- Looked up by ID for detail screens

---

## 🚀 Getting Started

### **Prerequisites**
- Android Studio Otter 3+ (2025.2.3)
- Kotlin 1.9+
- Gradle 8+

### **Build & Run**
```bash
# Clone repository
git clone https://github.com/HasnathAhmedTamim/myapp.git

# Open in Android Studio
# Sync Gradle
# Run on emulator/device
```

---

## 🧩 Key Takeaways

1. **NavGraph is the brain** - Manages state + navigation logic
2. **Screens are pure UI** - No navigation code, just callbacks
3. **Type-safe routes** - No string-based bugs via `@Serializable`
4. **UDF = predictable** - Data down, events up
5. **remember = stability** - State survives recomposition
6. **Back stack control** - `popUpTo` shapes navigation history
7. **Lazy composables** - Efficient rendering for lists/grids

---

## 📚 Further Reading
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Navigation in Compose](https://developer.android.com/jetpack/compose/navigation)
- [State Management](https://developer.android.com/jetpack/compose/state)
- [Material Design 3](https://m3.material.io/)

---

## 👤 Author
**HasnathAhmedTamim**  
GitHub: [@HasnathAhmedTamim](https://github.com/HasnathAhmedTamim)

---

## 📄 License
This project is for educational purposes.