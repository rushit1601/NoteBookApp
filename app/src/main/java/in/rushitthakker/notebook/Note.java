package in.rushitthakker.notebook;

/**
 * Created by silversurfer on 12/21/2016.
 */

public class Note {
    private String title,message;
    private long noteId,dateCreatedMilli;
    private Category noteCategory;

    public enum Category { PERSONAL,TECHNICAL,QUOTE,FINANCE}

    public Note (String title,String message,Category category){
        this.title =title;
        this.message =message;
        this.noteCategory =category;
        this.noteId = 0;
        this.dateCreatedMilli = 0;
    }

    public Note (String title,String message,Category category,long noteId, long dateCreatedMilli){
        this.title =title;
        this.message =message;
        this.noteCategory =category;
        this.noteId = noteId;
        this.dateCreatedMilli = dateCreatedMilli;
    }

    public String getTitle(){
        return title;
    }

    public String getMessage(){
        return message;
    }


    public Category getCategory(){
        return noteCategory;
    }

    public long getDate(){
        return dateCreatedMilli;
    }

    public long getId(){
        return noteId;
    }

    public String toString() {
        return "ID:" + noteId + "Title:" + title + "Message" + message +  "IconCategory" + noteCategory.name() + "Date in mill" + dateCreatedMilli;
    }

    public int getAssociatedDrwable(){
        return categoryToDrawable(noteCategory);
    }

    public static int categoryToDrawable(Category noteCategory){

        switch (noteCategory){
            case PERSONAL:
                return R.drawable.p;
            case TECHNICAL:
                return R.drawable.t;
            case FINANCE:
                return R.drawable.f;
            case QUOTE:
                return R.drawable.q;
        }

        return R.drawable.p;
    }


}
