package dsgp6.fakebook.Utils;

public class FuzzySearch {
    public static boolean contains(String target, String keyword) {
        target = target.toLowerCase();
        keyword = keyword.toLowerCase();

        if (target.isEmpty() || keyword.isEmpty()) {
            return false;
        }

        int keywordIndex = 0;
        for (int i = 0; i < target.length(); i++) {
            if (target.charAt(i) == keyword.charAt(keywordIndex)) {
                keywordIndex++;

                if (keywordIndex == keyword.length()) {
                    return true;
                }
            }
        }

        return false;
    }
}

