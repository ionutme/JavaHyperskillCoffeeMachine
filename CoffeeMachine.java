package machine;

import java.util.Scanner;
//import org.jetbrains.annotations.NotNull;

class State {
    int money = 550;
    int water = 400;
    int milk = 540;
    int beans = 120;
    int disposableCups = 9;
}

public class CoffeeMachine {

    enum Action {BUY, FILL, TAKE, REMAINING, EXIT}
    enum BuyOption { ESPRESSO, LATTE, CAPUCCINO, BACK}

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        var state = new State();

        while (true) {
            String input = getActionInput(scanner);

            if (takeAction(scanner, state, input)) {
                continue;
            }

            return;
        }
    }

    private static boolean takeAction(Scanner scanner, State state, String input) {
        Action action = Action.valueOf(input.toUpperCase());

        switch (action) {
            case BUY:
                buy(state, scanner);
                break;
            case FILL:
                fill(state, scanner);
                break;
            case TAKE:
                take(state);
                break;
            case REMAINING:
                printStatus(state);
                break;
            case EXIT:
                return false;
        }

        return true;
    }

    private static void fill(State state, Scanner scanner) {
        System.out.println("Write how many ml of water do you want to add:");
        state.water += scanner.nextInt();
        System.out.println("Write how many ml of milk do you want to add:");
        state.milk += scanner.nextInt();
        System.out.println("Write how many grams of coffee do you want to add:");
        state.beans += scanner.nextInt();
        System.out.println("Write how many disposable cups of coffee do you want to add:");
        state.disposableCups += scanner.nextInt();
    }

    private static void buy(State state, Scanner scanner) {
        String input = getBuyInput(scanner);
        BuyOption option = getBuyOption(input);

        if (!hasEnoughResources(state, option)) {
            return;
        }

        prepareCoffee(state, option);
    }

    private static void take(State state) {
        System.out.println("I gave you $" + state.money);
        state.money = 0;
    }

    private static void printStatus(State state) {
        System.out.println("The coffee machine has:");

        System.out.println(state.water + " of water");
        System.out.println(state.milk + " of milk");
        System.out.println(state.beans + " of coffee beans");
        System.out.println(state.disposableCups + " of disposable cups");
        System.out.println(state.money + " of money");
    }

    private static String getActionInput(Scanner scanner) {
        System.out.println("Write action (buy, fill, take, remaining, exit):");
        return scanner.next();
    }

    private static String getBuyInput(Scanner scanner) {
        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino:");
        return scanner.next();
    }

    private static BuyOption getBuyOption(String input) {
        switch (input) {
            case "1":
                return BuyOption.ESPRESSO;
            case "2":
                return BuyOption.LATTE;
            case "3":
                return BuyOption.CAPUCCINO;
            default:
                return BuyOption.BACK;
        }
    }

    private static void prepareCoffee(State state, BuyOption option) {
        switch (option) {
            case ESPRESSO:
                prepareEspresso(state);
                break;
            case LATTE:
                prepareLatte(state);
                break;
            case CAPUCCINO:
                prepareCapuccino(state);
                break;
            case BACK:
                return;
        }
    }

    private static void prepareCapuccino(State state) {
        state.water -= 200;
        state.milk -= 100;
        state.beans -= 12;
        state.money += 6;
        state.disposableCups--;
    }

    private static void prepareLatte(State state) {
        state.water -= 350;
        state.milk -= 75;
        state.beans -= 20;
        state.money += 7;
        state.disposableCups--;
    }

    private static void prepareEspresso(State state) {
        state.water -= 250;
        state.beans -= 16;
        state.money += 4;
        state.disposableCups--;
    }

    private static boolean hasEnoughResources(State state, BuyOption option) {

        String failReason = hasEnoughResources(option, state);
        if (failReason != null) {
            showNotEnoughResourcesMessage(failReason);
            return false;
        }

        showEnoughResourcesMessage();

        return true;
    }

    private static String hasEnoughResources(BuyOption option, State state) {
        if(state.disposableCups == 0) {
            return "disposable cups";
        }

        switch (option) {
            case BACK:
                return null;
            case ESPRESSO:
                return checkEspresso(state);
            case LATTE:
                return checkLatte(state);
            case CAPUCCINO:
                return checkCapuccino(state);
        }

        return null;
    }

    private static void showNotEnoughResourcesMessage(String failReason) {
        System.out.println("Sorry, not enough " + failReason + "!");
    }

    private static void showEnoughResourcesMessage() {
        System.out.println("I have enough resources, making you a coffee!");
    }

    private static String checkCapuccino(State state) {
        if (state.water - 200 < 0) {
            return "water";
        } else if (state.milk - 100 < 0) {
            return "milk";
        } else if (state.beans - 12 < 0) {
            return "beans";
        }

        return null;
    }

    private static String checkLatte(State state) {
        if (state.water - 350 < 0) {
            return "water";
        } else if (state.milk - 75 < 0) {
            return "milk";
        } else if (state.beans - 20 < 0) {
            return "beans";
        }

        return null;
    }

    private static String checkEspresso(State state) {
        if (state.water - 250 < 0) {
            return "water";
        } else if (state.beans - 16 < 0) {
            return "beans";
        }

        return null;
    }
}
