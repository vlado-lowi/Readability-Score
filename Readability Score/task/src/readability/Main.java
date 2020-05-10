package readability;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws Exception
    {
        String text;
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))){
            StringBuilder stringBuilder = new StringBuilder();
            br.lines().forEach(stringBuilder::append);
            text = stringBuilder.toString();
        } catch(Exception e) {
            return;
        }
        TextInfo textInfo = new TextInfo(text);
        textInfo.printInfo();
        getDesiredOutput(textInfo);

    }

    private static void getDesiredOutput(TextInfo textInfo) {
        String answer;
        try (Scanner in = new Scanner(System.in)){
            System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
            answer = in.next();

        } catch (Exception E) {
            System.err.println("Error duh...");
            return;
        }
        System.out.println();
        if ("ARI".equalsIgnoreCase(answer)) {
            textInfo.getARI();
        } else if ("FK".equalsIgnoreCase(answer)) {
            textInfo.getFK();
        } else if ("SMOG".equalsIgnoreCase(answer)) {
            textInfo.getSMOG();
        } else if ("CL".equalsIgnoreCase(answer)) {
            textInfo.getCL();
        } else if ("all".equalsIgnoreCase(answer)) {
            textInfo.getARI();
            textInfo.getFK();
            textInfo.getSMOG();
            textInfo.getCL();
        } else {
            System.err.println(answer + " not recognized try again.");
        }
        System.out.println();
    }
}
