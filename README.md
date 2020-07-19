# Contacts-and-Permissions-App
Asks user for permission to access contacts at run-time and dispays contacts list.

Asks users to access contacts list. If allowed, contact list is diplayed in a
listview otherwise snackbar is displayed notifying the user. Snackbar has an action 
button which when clicked, opens an AlertDialog again asking for permission. 
If user chooses to tick the box "Do not ask again" and denies the request, the 
permission is permanently denied. If the user clicks on the button to show
contact list, settings activity of the app opens and the user can navigate to
permissions section to grant permission. If permission is granted, clicking the 
button will show a list of user's contact list in a listview.
