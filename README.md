MealMate - Weekly Meal Planning App
MealMate is an intuitive meal planning and grocery management app designed to simplify meal organization and grocery tracking. It enables users to plan weekly meals, store recipes, generate grocery lists automatically, and manage their meals with advanced gesture controls.

Key Features
 User Authentication – Secure Firebase Authentication for login & registration.
 Planned Meal Management – Group meals weekly, add, edit, delete, and undo actions.
 Swipe-to-Delete & Shake-to-Undo – Swipe left to delete meals & groceries, and shake device to undo deletions within 3 seconds.
 Swipe-Right to Favorite Meals – Mark meals as favorites with a yellow star icon for quick access.
 Grocery List Management – Auto-fetch ingredients from recipes, manually add groceries, and update or delete items.
 Share Grocery List via SMS – Send grocery lists directly via SMS API.
 Recipe Management – Add recipes with meal type, preparation time, cook time, instructions, and ingredients.
 Profile Management – View, update, and upload a profile picture.
 Offline Support – Uses SQLite database for meal & grocery storage without requiring internet access.

Tech Stack
Programming Language: Java (Android)
Database: SQLite (Local Storage)
APIs Used: SMS API for sharing grocery lists
Architecture: MVVM (Model-View-ViewModel)
UI Framework: Material Design


Installation
Clone the repository:

sh
Copy
Edit
git clone https://github.com/yourusername/mealmate.git
cd mealmate
Open the project in Android Studio.

Sync dependencies and run the app on an emulator or physical device.

Future Enhancements
 Cloud Sync – Sync meal plans & groceries across multiple devices.
 Dark Mode & Custom Themes – Improve accessibility & user customization.
 Nutritional Insights – Fetch calorie & nutrition data for meals.
 Voice Input for Grocery & Recipe Entry – Enable hands-free interactions.
