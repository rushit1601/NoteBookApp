package in.rushitthakker.notebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by silversurfer on 12/21/2016.
 */

public class NoteAdapter extends ArrayAdapter<Note> {

    public NoteAdapter(Context context, ArrayList<Note> notes){
        super(context,0,notes);
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent){
        Note note = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row,parent,false);
        }

        //Get views by references
        TextView noteTitle = (TextView) convertView.findViewById(R.id.listItemNoteTitle);
        TextView noteBody = (TextView) convertView.findViewById(R.id.listItemNoteBody);
        ImageView noteIcon = (ImageView) convertView.findViewById(R.id.noteIcon);


        noteTitle.setText(note.getTitle());
        noteBody.setText(note.getMessage());
        noteIcon.setImageResource(note.getAssociatedDrwable());

        return convertView;

    }

}
