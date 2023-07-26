package me.blafexe.infoview;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Any implementing class or interface can be displayed in the infoview.
 */
public interface InfoviewElement {

    /**
     * The importance level determines, how high up in the infoview the element is displayed.
     * Higher values mean higher priority and therefore a higher position.
     * If set to 0, the element will be appended at the end of the list.
     * @return The importance level.
     */
    int getImportance();

    /**
     * A category can be used to group specific elements together.
     * The importance will be inherited from its most important element.
     * If the category's name starts with '?', the name will be hidden.
     * The text can be formatted using legacy minecraft formatting.
     * @return An optional describing the category or an empty optional if no category was defined.
     */
    Optional<String> getCategory();

    /**
     * This text will always be displayed in the infoview.
     * It can be formatted using minecraft legacy formatting. '\n' can be used to create line breaks.
     * @return The display text.
     */
    @NotNull String getText();

    /**
     * The id is used to later reference the element.
     * @return The element's (infoview) id.
     */
    @NotNull String getId();

}
