package me.blafexe.infoview;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Is used to convert a given list of <code>InfoviewElements</code> into a format that can be simply displayed on a scoreboard.
 */
public class InfoviewRenderer {

    /**
     * Converts all text provided by the elements into scoreboard entries.
     * @param elements List of <code>InfoviewElements</code> to be displayed.
     * @return A list of Strings, stripped of all formatting information but legacy minecraft formatting. Ready to be used
     * to insert into a scoreboard.
     */
    public List<String> render(List<InfoviewElement> elements) {

        List<String> strings = new LinkedList<>();

        //Used to create unique blank lines as dividers
        BlankLineCreator blankLineCreator = new BlankLineCreator();
        //Last used category
        String lastCategory = null;

        for (InfoviewElement element : elements) {

            String elementCategory = element.getCategory().isPresent() ? element.getCategory().get() : null;

            if (!Objects.equals(lastCategory, elementCategory)) {
                lastCategory = elementCategory;
                strings.add(blankLineCreator.next());
                if (isElementCategoryHidden(elementCategory)) strings.add(elementCategory);
            }
            strings.addAll(Arrays.asList(convertElementText(element.getText())));

        }

        return strings;

    }

    /**
     * Converts text provided by <code>InfoviewElements</code> to plain strings ready to be used by the Minecraft scoreboard.
     * @param elementText String provided by the <code>InfoviewElement's getText()</code>-method.
     * @return Plain String, ready for proper use in scoreboard.
     */
    private String[] convertElementText(String elementText) {
        //Cut strings at \n
        String[] strings = elementText.split("\n");
        for (int i = 0; i < strings.length; i++) {
            strings[i] = strings[i].replace("\n", "");
        }
        return strings;
    }

    /**
     * @param elementTitle String provided by <code>InfoviewElement's getCategory</code>-method.
     * @return True, if category name is meant to be hidden.
     */
    private boolean isElementCategoryHidden(String elementTitle) {
        return elementTitle != null && !elementTitle.startsWith("?");
    }

    /**
     * Is used to create Strings, that do not display anything but are separate from one another using different amounts
     * of blank spaces.
     */
    private static class BlankLineCreator {

        private int i;

        public BlankLineCreator() {
            i = 1;
        }

        /**
         * @return Next (unique) blank line.
         */
        public String next() {
            return " ".repeat(i++);
        }

    }

}
