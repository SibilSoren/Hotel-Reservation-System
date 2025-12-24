//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import api.MainMenu;

void main() {
    // TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the
    // highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    IO.println(String.format("Welcome to Sibil Hotel System"));
    MainMenu mainMenu = new MainMenu();
    mainMenu.runMenu();
}
