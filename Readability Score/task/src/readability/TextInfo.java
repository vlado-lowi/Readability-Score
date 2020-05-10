package readability;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextInfo {
    private int words = 0;
    private int sentences = 0;
    private int characters = 0;
    private int syllables = 0;
    private int polysyllables = 0;
    private String text;

    public TextInfo(String text) {
        this.text = text;
        this.setTextInfo();
    }

    private void setTextInfo() {
        // pattern matches any sentence
        Pattern pattern = Pattern.
                compile("([\\s\\S]+?[.!?]|[\\s\\S]+?[.!?]*$)");
        Matcher matcher = pattern.matcher(text);

        //iterate over all sentences
        while (matcher.find()) {
            String sentence = matcher.group();
            sentences++;

            // pattern matches any printable character
            Pattern pattern1 = Pattern.compile("[^\" \"\\n\\t]");
            Matcher matcher1 = pattern1.matcher(sentence);
            characters += getCountOfMatches(matcher1);

            // pattern matches any word
            Pattern pattern2 = Pattern.compile("(\\b\\d+,\\d+\\b)|(\\b\\w+\\b)");
            Matcher matcher2 = pattern2.matcher(sentence);
            // iterate over all words
            while (matcher2.find()) {
                words++;
                String word = matcher2.group();
                int wordSyllables = getWordSyllables(word);
                syllables += wordSyllables > 0 ? wordSyllables : 1;
                if (wordSyllables > 2   ){
                    polysyllables++;
                }
            }
        }
    }

    /*
           count syllables procedure
          1. Count the number of vowels in the word.
          2. Do not count double-vowels (for example, "rain" has 2 vowels but is only 1 syllable)
          3. If the last letter in the word is 'e' do not count it as a vowel (for example, "side" is 1 syllable)
          4. If at the end it turns out that the word contains 0 vowels, then consider this word as 1-syllable.
     */
    private int getWordSyllables(String word) {
        int wordSyllables = 0;
//                    true if previous character in word is vowel
        boolean prevCharVowel = false;
//                    iterate over character of word
        for (int i = 0; i < word.length(); i++) {
//                        count vowels
            if (isVowel(word.charAt(i))) {
                if (!prevCharVowel) {
                    if (i + 1 >= word.length()){
//                                    last character of word
                        if (word.charAt(i) != 'e' && word.charAt(i) != 'E'){
                            wordSyllables++;
                        }
                    } else {
                        wordSyllables++;
                        prevCharVowel = true;
                    }
                }
            } else {
                prevCharVowel = false;
            }
        }
        return wordSyllables;
    }

    public void getARI() {
        double score = 4.71D * (characters / (words + 0D))
                + 0.5D * (words / (sentences + 0D)) - 21.43;
        Map<Integer, String> difficultyMap = getDifficultyMap();
        String difficulty = difficultyMap.get((int) Math.round(score));
        System.out.printf("Automated Readability Index: %.2f (about %s year olds).%n", score, difficulty);

    }

    public void getFK(){
        double score = 0.39D * (words / (sentences + 0D)) + 11.8D * (syllables / (words + 0D)) - 15.59D;
        Map<Integer, String> difficultyMap = getDifficultyMap();
        String difficulty = difficultyMap.get((int) Math.round(score));
        System.out.printf("Flesch–Kincaid readability tests: %.2f (about %s year olds).%n", score, difficulty);
    }

    public void getSMOG(){
        double score = 1.043D * Math.sqrt(polysyllables * (30 / (sentences + 0D))) + 3.1291D;
        Map<Integer, String> difficultyMap = getDifficultyMap();
        String difficulty = difficultyMap.get((int) Math.round(score));
        System.out.printf("Simple Measure of Gobbledygook: %.2f (about %s year olds).%n", score, difficulty);
    }

    public void getCL(){
        double L = characters / (words + 0D) * 100D;
        double S = sentences / (words + 0D) * 100D;
        double score = 0.0588D * L - 0.296D * S - 15.8D;
        Map<Integer, String> difficultyMap = getDifficultyMap();
        String difficulty = difficultyMap.get((int) Math.round(score));
        System.out.printf("Coleman–Liau index: %.2f (about %s year olds).%n", score, difficulty);
    }

    private static Map<Integer, String> getDifficultyMap() {
        Map<Integer, String> difficultyMap = new HashMap<>();
        difficultyMap.put(1,"6");
        difficultyMap.put(2,"7");
        difficultyMap.put(3,"9");
        difficultyMap.put(4,"10");
        difficultyMap.put(5,"11");
        difficultyMap.put(6,"12");
        difficultyMap.put(7,"13");
        difficultyMap.put(8,"14");
        difficultyMap.put(9,"15");
        difficultyMap.put(10,"16");
        difficultyMap.put(11,"17");
        difficultyMap.put(12,"18");
        difficultyMap.put(13,"24");
        difficultyMap.put(14,"24+");
        return difficultyMap;
    }
    public void printInfo(){
        System.out.println("Words: " + words);
        System.out.println("Sentences: " + sentences);
        System.out.println("Characters: " + characters);
        System.out.println("Syllables: " + syllables);
        System.out.println("Polysyllables: " + polysyllables);
    }
    private static boolean isVowel(char ch) {
        if(ch == 'a' || ch == 'A') {
            return true;
        } else if(ch == 'e' || ch == 'E') {
            return true;
        } else if(ch == 'i' || ch == 'I') {
            return true;
        } else if(ch == 'o' || ch == 'O') {
            return true;
        } else if(ch == 'u' || ch == 'U') {
            return true;
        } else return ch == 'y' || ch == 'Y';
    }

    private static int getCountOfMatches(Matcher matcher) {
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.setTextInfo();
    }
}
