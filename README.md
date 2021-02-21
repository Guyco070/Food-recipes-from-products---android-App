# Food-recipes-from-products - (A surprise meal) "ארוחה בהפתעה"

* The code was written using Android studio workspace, FireBase database and Java, XML languages. 

* The application is intended for use in the Hebrew language.

* Link to the end to end video - https://drive.google.com/file/d/19Vdt2qTgr952Gfri7tZY9gCDi6OTXrMI/view?usp=sharing.

* Notes:
  1) Self-learning without prior knowledge of Android Studio / FireBase / Java / XML.
  2) The app made before advanced OOP course (Java) and DB course (Sql and XML).
  3) The code includes notes containing code from previous versions as well as some examples of local data entry / from/to the database.
  4) The database is not full, so some of the images are white (temporary) and some of the recipes are repeated to simulate a fuller database - will be corrected soon.
  5) The project was uploaded via GitKraken.

General description of the application:
The user creates a list of food products which can search for in the database, the system will display the possible recipes for preparation using the same food products or some according to the user's request and even save desired recipes in favorites.
The app is intended for kosher meat-dairy kosher and adjusts the list of  in the search according to the ingredients already selected.

Detailed description:
1) Search:
- When writing a string, items starting with that string will be displayed.
- When there is at least one dairy ingredient in the selected ingredients, they will not find meat products in the search, and similarly in the case of the existence of a meat product in the list of selected ingredients (a message pops up inform for this and disappears).
- Selecting a product will put it in the list and clicking on the "-" mark in the product corner in the list will remove it and adjust the search list to the new list of selected ingredients.

2) Side menu:
- filtering:
  - "Show products by basic product only" - allows to find recipes by ingredients that require for the recipe (ingredients marked with * in the recipe). For example - bread and minced meat will allow the presentation of a hamburger recipe even though other ingredients are missing (tomato and lettuce).
  - "Allow a number of deficiencies" - Allows to find recipes by a smaller number of ingredients according to the user's choice. For example - choosing one lack, bread and minced meat and lettuce will allow the presentation of a recipe for a hamburger even though missing one ingredient (lettuce).
  - "Filter by Chef" - Allows you to filter recipes by chef's name.
  - "Filter by calories" - allows filtering recipes by a maximum number of calories.

- Personal customization:
  - "Favorites" - Displays the recipes marked as favorites.
  - "Stopwatch / Timer" - Activating the clock when it is reset will activate the stopwatch and activating the clock after entering a value (numeric only) in the text box will activate a timer according to the radio button marked (hours / minutes / seconds).

3) Insertion/removal to/from favorites will be done by clicking on the star at the right corner at the preview of the recipe.

4) Clicking on a preview will open a pop-up window with the full recipe and all the details about it (for example - preparation time and calories).

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

* הקוד נכתב בשימוש בסביבת העבודה Android Studio, במאגר הנתונים של FireBase ובשפות - Java, XML.

* האפליקציה נועדה לשימוש בשפה העברית. 

* קישור לסרטון End to end - https://drive.google.com/file/d/19Vdt2qTgr952Gfri7tZY9gCDi6OTXrMI/view?usp=sharing.

* הערות:
  1) למידה עצמית ללא ידע קודם ב - Android studio / FireBase / Java / XML.
  2) האפליקציה נכתבה לפ י ביצוע קורס תכנות מונחה עצמים מתקדם (Java) וקורס בסיסי נתונים (Sql and XML).
  3) האפליקצייה כוללת הערות בהם קוד של גרסאות קודמות וגם כמה דוגמאות להכנסת נתונים מקומית / ממסד הנתונים / למסד הנתונים.
  4) מאגר הנתונים אינו מלא ולכן חלק מהתמונות הינן תמונה לבנה (זמנית) וחלק מהמתכונים חוזרים על עצמם לשם הדמייה של מאגר נתונים מלא יותר - יתוקן בהקדם.
  5) הקוד הועלה באמצעות GitKraken.

תיאור כללי של האפליקציה:
המשתמש יוצר רשימה של מרכיבים אשר אותם הוא יכול לחפש במאגר הנתונים, המערכת תציג את המתכונים האפשריים להכנה באמצעות אותם מרכיבים או חלקם בהתאם לבקשת המשתמש ואף לשמור מתכונים רצויים במועדפים.
האפליקציה מיועדת לשומרי כשרות בשר-חלב ומתאימה את רשימת המרכיבים בחיפוש על פי המרכיבים שכבר נבחרו.

תיאור מפורט:
  1) חיפוש:
    - בכתיבת מחרוזת יוצגו פריטים המתחילים במחרוזת זו.
    - כאשר במרכיבים שנבחרו קיים מרכיב חלבי אחד לפחות לא ימצאו בחיפוש מוצרים בשריים, ובדומה גם במקרה של קיום מוצר בשרי ברשימת המרכיבים שנבחרו (מוקפצת הודעה המתריעה על כך ונעלמת).
    - בחירה במוצר תכניס אותו לרשימה ולחיצה על סימ ה- "-" בפינת המוצר ברשימה תסיר אותו ותתאים את רשימת החיפוש לרשימה החדש של המרכיבים שנבחרו.

  2) תפריט צידי:
  
  - סינון:
    - "הצג מוצרים לפי מוצר בסיסי בלבד" - מאפשר מציאת מתכונים לפי מרכיבים אשר חייב אותם עבור המתכון (מרכיבים המסומנים ב-* במתכון). לדוגמא - לחם ובשר טחון יאפשרו הצגת מתכון להמבורגר למרות שחסרים מרכיבים אחרים (עגבנייה וחסה).
    - "אפשר מספר חוסרים" -  מאפשר מציאת מוצרים לפי מספר מרכיבים מצומצם יותר על פי בחירת המשתמש. לדוגמא - בבחירת חוסר 1, לחם ובשר טחון וחסה יאפשרו הצגת מתכון להמבורגר למרות שחסר מרכיב 1 (חסה).
    - "סנן לפי שף" - מאפשר סינון מתכונים לפי שם שף.
    - "סנן לפי קלוריות" - מאפשר סינון מתכונים לפי מספר קלוריות מקסימאלי.

  - התאמה אישית:
    - "מועדפים" - הצגת המתכונים המסומנים כמועדפים.
    - "סטופר/טיימר" - הפעלת השעון כאשר הוא מאופס תפעיל סטופר והפעלת השעון לאחר הכנסת ערך (רק מספרי) בתיבת הטקסט תפעיל טיימר בהתאם לכפתור הרדיו המסומן (שעות / דקות / שניות).

  3) הכנסה/הסרה למועדפים/מהמועדפים תבוצע באמצעות לחיצה על הכוכב הנמצא בפינה הימנית בהצגה המוקדמת של המתכון.

  4) לחיצה על תצוגה מתקדימה תפתח חלון קופץ (pop up) ובו המתכון המלא וכל הפרטים עליו למשל - זמן הכנה וקלוריות).
