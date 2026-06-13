package service.impl;

import service.OutputService;

import java.util.List;

public class OutputServiceImpl implements OutputService {
    @Override
    public void displayMainMenu() {
        System.out.println("############# Main Menu #############\n1) Create order\n2) Close order\n3) All open orders\n4) Statistics");
        System.out.print("Choose Main Menu option: ");
    }

    @Override
    public void displayList(List<?> list) {
        System.out.println("###########################");
        for (int i = 1; i <=list.size(); i++) {
            System.out.println(i + ") " + list.get(i-1));
        }
        System.out.println("###########################");
    }

    @Override
    public void displayWrongInputMessage() {
        System.out.println("Wrong input");
    }

    @Override
    public void displayUserNameInputMessage() {
        System.out.print("Input UserName: ");
    }

    @Override
    public void displayExceptionMessage(String exceptionMessage) {
        System.out.println(exceptionMessage);
    }

    @Override
    public void displayBookSearchInputMessage() {
        System.out.print("Input key search words for book (name or author): ");
    }

    @Override
    public void displayPositionNumber() {
        System.out.print("Choose the option by entering its number in the list: ");
    }

}
