# MyApp - Complete Technical Documentation

## 📱 Project Overview
A modern Android application built with **Jetpack Compose** and **Material Design 3**, featuring comprehensive user authentication with OTP verification, persistent local database storage, session management, and a multi-screen user interface.

---

## 🏗️ Architecture

### **Tech Stack**
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Navigation:** Type-safe Compose Navigation
- **Design System:** Material Design 3
- **Build Tool:** Gradle
- **Database:** Room (SQLite)
- **Persistence:** DataStore (Session Management)
- **Coroutines:** For async operations
- **State Management:** MutableState, StateFlow

### **Architecture Pattern**
- **MVVM (Model-View-ViewModel)** with Repository pattern
- **Unidirectional Data Flow (UDF)**
    - Data flows DOWN: Repository → ViewModel → Screen
    - Events flow UP: User actions → ViewModel → Repository
- **Single Source of Truth**: Room Database for user data, DataStore for session
- **Separation of Concerns**: 
    - **Screens**: Pure UI (Composables)
    - **ViewModels**: Business logic & state management
    - **Repository**: Data access abstraction
    - **Database**: Data persistence layer

---

## 📂 Project Structure

```
com.example.myapp/
├── MainActivity.kt                    # Entry point, hosts Compose UI
│
├── navigation/
│   ├── NavGraph.kt                   # Navigation orchestrator & state management
│   └── Screen.kt                     # Type-safe route definitions (Serializable)
│
├── screens/
│   ├── auth/                         # Authentication screens
│   │   ├── LoginScreen.kt           # User login with username/password
│   │   ├── SignupScreen.kt          # New user registration with validation
│   │   ├── ForgotPasswordScreen.kt  # Phone number entry for password reset
│   │   ├── OtpVerificationScreen.kt # OTP verification (fixed OTP: 111111)
│   │   └── ResetPasswordScreen.kt   # Set new password
│   │
│   ├── main/                         # Main app screens (future expansion)
│   │
│   ├── HomeScreen.kt                # Main dashboard with 4 cards
│   ├── ProfileScreen.kt             # User profile with logout & delete account
│   ├── SearchScreen.kt              # Search functionality
│   ├── CardDetailScreen.kt          # Individual card details
│   ├── AlbumScreen.kt               # Album list view
│   ├── AlbumDetailScreen.kt         # Album overview
│   └── AlbumItemDetailScreen.kt     # Individual album item
│
├── viewmodel/
│   ├── AuthViewModel.kt             # Authentication business logic
│   └── ProfileViewModel.kt          # Profile management logic
│
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt          # Room database configuration
│   │   ├── dao/
│   │   │   └── UserDao.kt          # User data access object (CRUD operations)
│   │   └── entity/
│   │       └── UserEntity.kt       # User table schema
│   │
│   ├── repository/
│   │   └── UserRepository.kt       # Data access abstraction layer
│   │
│   └── preferences/
│       └── SessionManager.kt       # DataStore-based session persistence
│
├── model/
│   ├── User.kt                     # User data model (for UI layer)
│   └── AlbumItem.kt                # Album data model
│
├── util/
│   └── ValidationUtil.kt           # Input validation utilities
│
├── components/                      # Reusable UI components
│
└── ui/
    └── theme/                       # Material Design 3 theme configuration
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

### **4. Profile Management**

```
ProfileScreen receives userId from SessionManager
    ↓
ProfileViewModel.loadUserProfile(userId)
    ↓
UserRepository.getUserById(userId)
    ↓
Room Database: SELECT * FROM users WHERE userId = ?
    ↓
User data displayed in ProfileScreen
```

#### **Logout Action**
```
User taps "Logout" button
    ↓
Confirmation dialog appears
    ↓
User confirms → ProfileViewModel.logout()
    ↓
SessionManager.clearSession()
    ↓
DataStore clears all preferences
    ↓
Navigate to LoginScreen {
  popUpTo(0) { inclusive = true }
}
```

#### **Delete Account Action**
```
User taps "Delete Account" button
    ↓
Confirmation dialog: "Are you sure?"
    ↓
User confirms → ProfileViewModel.deleteAccount(userId)
    ↓
UserRepository.deleteUser(userId)
    ↓
Room Database: DELETE FROM users WHERE userId = ?
    ↓
SessionManager.clearSession()
    ↓
Navigate to LoginScreen
    ↓
User cannot login anymore (no database record)
```

---

### **5. Navigation Patterns**

#### **Pattern 1: Authentication Flow**
```kotlin
// Signup → Login
navigate(Screen.Login) {
  popUpTo<Screen.Signup> { inclusive = true }
}

// Login → Home (Clear all auth screens)
navigate(Screen.Home) {
  popUpTo<Screen.Login> { inclusive = true }
}

// Forgot Password → OTP → Reset → Login
// Each step replaces previous to prevent back navigation
```

#### **Pattern 2: Simple Navigation (Profile)**
```kotlin
// From HomeScreen
onProfileClick()
    ↓
navController.navigate(Screen.Profile)
    ↓
Stack: [Home] → [Profile]
```

#### **Pattern 3: Bottom Tab Navigation**
```kotlin
// From Search → Album
navigate(Screen.Album) {
  popUpTo<Screen.Home>() // Clear everything above Home
}
    ↓
Stack: [Home] → [Album]
```

#### **Pattern 4: Nested Detail Navigation**
```kotlin
// AlbumScreen → AlbumDetailScreen → AlbumItemDetailScreen
1. Album list click → Screen.AlbumDetail
2. Album detail item click → Screen.AlbumItemDetail(id, title)
3. Back navigation pops one level at a time
```

---

### **6. Type-Safe Navigation**

#### **Route Definitions (`Screen.kt`)**
```kotlin
@Serializable
sealed interface Screen {
    // Authentication
    @Serializable data object Login
    @Serializable data object Signup
    @Serializable data object ForgotPassword
    @Serializable data object OtpVerification
    @Serializable data object ResetPassword
    
    // Main App
    @Serializable data object Home
    @Serializable data object Profile
    @Serializable data object Search
    @Serializable data object Album
    
    // Detail Screens
    @Serializable data class CardDetail(
        val cardId: String,
        val cardTitle: String
    )
    @Serializable data class AlbumItemDetail(
        val itemId: String,
        val itemTitle: String
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

### **7. Back Navigation Flow**

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

### **Authentication Screens**

#### **LoginScreen**
- **State:** `username`, `password` (local)
- **ViewModel:** AuthViewModel
- **Actions:** Login, Navigate to Signup/Forgot Password
- **Validation:** Username & password required
- **Navigation Out:** → Home (clears login from stack)
- **Features:** Visual feedback for success/error

#### **SignupScreen**
- **State:** `username`, `email`, `phone`, `password`, `confirmPassword`
- **ViewModel:** AuthViewModel
- **Validation:** 
  - Username: 3-20 chars, alphanumeric + underscore, unique
  - Email: Optional, valid format
  - Phone: 11 digits, Bangladesh format (01[3-9]XXXXXXXX)
  - Password: 6-50 chars
  - Confirm Password: Must match
- **Database:** Creates new UserEntity with isVerified=true
- **Navigation:** → Login on success
- **Features:** Real-time validation feedback

#### **ForgotPasswordScreen**
- **State:** `phoneNumber` (local)
- **Validation:** 11-digit Bangladesh phone
- **Navigation:** → OtpVerificationScreen
- **Features:** Phone number formatting

#### **OtpVerificationScreen**
- **State:** `otpCode` (6 digits)
- **Validation:** Fixed OTP = "111111"
- **Navigation:** → ResetPasswordScreen on success
- **Features:** Visual OTP input

#### **ResetPasswordScreen**
- **State:** `newPassword`, `confirmPassword`
- **ViewModel:** AuthViewModel
- **Database:** Updates password for phone number
- **Navigation:** → Login on success
- **Note:** Affects all users with same phone number

---

### **Main App Screens**

#### **HomeScreen**
- **Receives:** `username` from SessionManager
- **Events:** 5 callbacks (profile, home, search, album, card clicks)
- **UI:** 4-card grid + top/bottom bars
- **Navigation:** Pushes ProfileScreen, CardDetailScreen, SearchScreen, AlbumScreen
- **Features:** Material Design 3 cards with elevation

#### **ProfileScreen**
- **ViewModel:** ProfileViewModel
- **Data Source:** Room Database via SessionManager
- **Displays:** Username, Email, Phone, Account Created Date
- **Actions:**
  - **Logout:** Clears session, returns to Login
  - **Delete Account:** Removes user from database permanently
- **Confirmations:** Both actions require user confirmation
- **Navigation:** → Login on logout/delete

#### **CardDetailScreen**
- **Receives:** `cardId`, `cardTitle` (from route args)
- **Events:** `onBack`
- **UI:** Displays card info
- **Navigation:** Pops back to Home

#### **SearchScreen**
- **State:** `searchQuery` (local)
- **Events:** Back, home, album navigation
- **UI:** Search bar (placeholder)
- **Navigation:** Bottom nav integration

#### **AlbumScreen**
- **Receives:** `albumItems` list
- **Events:** Item clicks, detail button, back/nav
- **UI:** Scrollable list of album cards
- **Navigation:** → AlbumDetailScreen, AlbumItemDetailScreen

#### **AlbumDetailScreen**
- **Receives:** Full `albumItems` list
- **Events:** Item clicks, back
- **UI:** Grid of all albums

#### **AlbumItemDetailScreen**
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

### **UserEntity.kt** (Database Layer)
```kotlin
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    @ColumnInfo(name = "userId")
    val userId: Long,
    
    @ColumnInfo(name = "username")
    val username: String,
    
    @ColumnInfo(name = "email")
    val email: String?,
    
    @ColumnInfo(name = "phoneNumber")
    val phoneNumber: String,
    
    @ColumnInfo(name = "password")
    val password: String,
    
    @ColumnInfo(name = "isVerified")
    val isVerified: Boolean = true,
    
    @ColumnInfo(name = "createdAt")
    val createdAt: Long = System.currentTimeMillis()
)
```
- **Constraints:** Unique username
- **Auto-verification:** All users auto-verified (isVerified = true)
- **Multiple users:** Can share same phone number

### **User.kt** (UI Layer)
```kotlin
data class User(
    val userId: Long,
    val username: String,
    val email: String?,
    val phoneNumber: String,
    val isVerified: Boolean,
    val createdAt: Long
)
```
- Mapped from UserEntity
- Used in ViewModels and UI
- Excludes sensitive data (password)

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

## 🗄️ Database Architecture

### **AppDatabase.kt**
```kotlin
@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "myapp_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
            }
        }
    }
}
```

### **UserDao.kt** - Available Operations
```kotlin
interface UserDao {
    // Create
    @Insert
    suspend fun insertUser(user: UserEntity): Long
    
    // Read
    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserById(userId: Long): UserEntity?
    
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): UserEntity?
    
    @Query("SELECT * FROM users WHERE phoneNumber = :phoneNumber")
    suspend fun getUsersByPhoneNumber(phoneNumber: String): List<UserEntity>
    
    // Update
    @Update
    suspend fun updateUser(user: UserEntity)
    
    @Query("UPDATE users SET password = :password WHERE phoneNumber = :phoneNumber")
    suspend fun updatePasswordByPhone(phoneNumber: String, password: String)
    
    // Delete
    @Query("DELETE FROM users WHERE userId = :userId")
    suspend fun deleteUser(userId: Long)
    
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE username = :username)")
    suspend fun isUsernameExists(username: String): Boolean
}
```

---

## 💾 Session Management

### **SessionManager.kt** (DataStore)
```kotlin
class SessionManager(private val context: Context) {
    
    // Stored Data
    data class SessionData(
        val isLoggedIn: Boolean = false,
        val userId: Long? = null,
        val username: String? = null,
        val phoneNumber: String? = null,
        val lastLogin: Long? = null
    )
    
    // Key Operations
    suspend fun saveLoginSession(userId: Long, username: String, phoneNumber: String)
    suspend fun clearSession()
    suspend fun updateUsername(newUsername: String)
    suspend fun getCurrentSession(): SessionData
    suspend fun checkLoginStatus(): Boolean
    
    // Reactive Flows
    val sessionData: Flow<SessionData>
    val isLoggedIn: Flow<Boolean>
    val userId: Flow<Long?>
    val username: Flow<String?>
}
```

**Persistence:**
- Survives app restarts
- Thread-safe with coroutines
- Reactive with Kotlin Flow
- Auto-login on app start if session exists

---

## ✅ Input Validation

### **ValidationUtil.kt**

| Field | Rules | Example Valid | Example Invalid |
|-------|-------|---------------|-----------------|
| **Username** | 3-20 chars, alphanumeric + underscore | `john_doe`, `user123` | `ab`, `user@name` |
| **Email** | Valid format (optional) | `user@gmail.com`, `` | `invalid.email` |
| **Phone** | 11 digits, BD format | `01712345678` | `123456`, `02123456789` |
| **Password** | 6-50 characters | `admin123` | `12345` |
| **OTP** | Exactly 6 digits | `111111` | `1234`, `abc123` |

**BD Phone Operators Supported:**
- Grameenphone: 017
- Robi: 018
- Banglalink: 019
- Teletalk: 015
- Airtel: 016
- Others: 013, 014

**Functions:**
```kotlin
fun validateUsername(username: String): ValidationResult
fun validateEmail(email: String): ValidationResult
fun validatePhoneNumber(phone: String): ValidationResult
fun validatePassword(password: String): ValidationResult
fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult
fun validateOtp(otp: String): ValidationResult
fun formatPhoneNumber(phone: String): String // Returns 01712-345678
fun sanitizePhoneNumber(phone: String): String // Removes spaces/dashes
```

---

## 🚀 Getting Started

### **Prerequisites**
- Android Studio Otter 3+ (2025.2.3)
- Kotlin 1.9+
- Gradle 8+
- Min SDK: 24 (Android 7.0)

### **Dependencies**
```kotlin
// Jetpack Compose
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.activity:activity-compose")

// Navigation
implementation("androidx.navigation:navigation-compose")

// Room Database
implementation("androidx.room:room-runtime")
implementation("androidx.room:room-ktx")
ksp("androidx.room:room-compiler")

// DataStore
implementation("androidx.datastore:datastore-preferences")

// Lifecycle & ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
implementation("androidx.lifecycle:lifecycle-runtime-compose")
```

### **Build & Run**
```bash
# Clone repository
git clone https://github.com/HasnathAhmedTamim/myapp.git

# Open in Android Studio
# Sync Gradle
# Run on emulator/device
```

### **Default Test Account**
After signup, you can create any account. Example:
- Username: `hasnath`
- Password: `Admin123`
- Phone: `01756401520`

---

## 🧩 Key Takeaways

1. **MVVM Architecture** - Clear separation: UI → ViewModel → Repository → Database
2. **Room Database** - Persistent local storage with type-safe queries
3. **DataStore Session** - Secure session management with reactive Flows
4. **Type-safe Navigation** - No string-based bugs via `@Serializable`
5. **Input Validation** - Comprehensive validation for all user inputs
6. **UDF Pattern** - Predictable data flow: Repository → ViewModel → UI
7. **Authentication Flow** - Complete signup/login/forgot password with OTP
8. **Profile Management** - Logout and delete account functionality
9. **Back Stack Control** - `popUpTo` shapes navigation history
10. **Coroutines** - All database operations are async and non-blocking

---

## 🔒 Security Considerations

### **Current Implementation**
- ✅ Username uniqueness enforced
- ✅ Session management with DataStore
- ✅ Input validation on all fields
- ⚠️ Passwords stored in plain text (not production-ready)

### **Production Recommendations**
- 🔐 Hash passwords with BCrypt/Argon2
- 🔐 Implement real OTP service (SMS/Email)
- 🔐 Add rate limiting for login attempts
- 🔐 Use proper encryption for sensitive data
- 🔐 Implement token-based authentication (JWT)

---

## 🐛 Known Issues & Limitations

1. **Password Reset:** Updates all users with the same phone number
2. **OTP Hardcoded:** Fixed to "111111" (no real SMS integration)
3. **Email Optional:** Email field is not required during signup
4. **No Password Recovery:** Forgot username feature not implemented
5. **Plain Text Passwords:** Not suitable for production use

---

## 🔮 Future Enhancements

- [ ] Implement real SMS OTP service
- [ ] Add password hashing (BCrypt)
- [ ] Email verification
- [ ] Forgot username functionality
- [ ] Profile picture upload
- [ ] Change password in profile
- [ ] Two-factor authentication (2FA)
- [ ] Social media login (Google, Facebook)
- [ ] Export user data
- [ ] Dark mode theme support
- [ ] Biometric authentication

---

## 📚 Further Reading
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Navigation in Compose](https://developer.android.com/jetpack/compose/navigation)
- [State Management](https://developer.android.com/jetpack/compose/state)
- [Material Design 3](https://m3.material.io/)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)

---

## 👤 Author
**HasnathAhmedTamim**  
GitHub: [@HasnathAhmedTamim](https://github.com/HasnathAhmedTamim)

---

## 📄 License
This project is for educational purposes.