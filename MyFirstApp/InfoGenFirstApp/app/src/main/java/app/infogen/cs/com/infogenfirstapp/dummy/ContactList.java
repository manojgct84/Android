package app.infogen.cs.com.infogenfirstapp.dummy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 9/24/2017.
 */
public class ContactList {

    public static final List<ContentItem> ITEMS = new ArrayList<ContentItem>();

    private static void addItem(ContentItem item) {
        ITEMS.add(item);
    }


    public static class ContentItem {
        public final String id;
        public final String phoneNumner;
        public final String displayName;

        public ContentItem(String id, String phoneNumner, String displayName) {
            this.id = id;
            this.phoneNumner = phoneNumner;
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return phoneNumner;
        }
    }
}
