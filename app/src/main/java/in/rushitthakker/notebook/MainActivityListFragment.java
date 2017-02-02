package in.rushitthakker.notebook;


import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityListFragment extends ListFragment {
    private ArrayList<Note> notes;
    private NoteAdapter noteAdapter;

    @Override
    public void onActivityCreated(Bundle BundleInstance){
        super.onActivityCreated(BundleInstance);


        NoteBookDbAdapter dbAdapter = new NoteBookDbAdapter(getActivity().getBaseContext());
        dbAdapter.open();
        notes = dbAdapter.getAllNotes();
        dbAdapter.close();

        noteAdapter = new NoteAdapter(getActivity(),notes);
        setListAdapter(noteAdapter);

        registerForContextMenu(getListView());
    }

    @Override
    public void onListItemClick(ListView l,View v,int position, long id){
        super.onListItemClick(l,v,position,id);

        launchNoteDetailActivity(MainActivity.FragmentType.VIEW,position);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,v,menuInfo);

        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.long_press_menu,menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int rowPosition = info.position;
        Note note = (Note) getListAdapter().getItem(rowPosition);
        switch (item.getItemId()){
            case R.id.edit:
                Log.d( TAG, "onContextItemSelected: ");
                launchNoteDetailActivity(MainActivity.FragmentType.EDIT,rowPosition);
                return true;
            case R.id.delete:
                NoteBookDbAdapter dbAdapter = new NoteBookDbAdapter(getActivity().getBaseContext());
                dbAdapter.open();
                dbAdapter.deleteNote(note.getId());
                return true;
        }

        return super.onContextItemSelected(item);
    }

    private void launchNoteDetailActivity(MainActivity.FragmentType ft,int position){
        Note note = (Note) getListAdapter().getItem(position);

        Intent intent = new Intent(getActivity(),NoteDetailActivity.class);
        intent.putExtra(MainActivity.NOTE_TITLE_EXTRA,note.getTitle());
        intent.putExtra(MainActivity.NOTE_MESSAGE_EXTRA,note.getMessage());
        intent.putExtra(MainActivity.NOTE_CATEGORY_EXTRA,note.getCategory());
        intent.putExtra(MainActivity.NOTE_ID_EXTRA,note.getId());

        switch (ft){
            case VIEW:
                intent.putExtra(MainActivity.NOTE_FRAGMENT_TYPE_EXTRA,MainActivity.FragmentType.VIEW);
                break;
            case EDIT:
                intent.putExtra(MainActivity.NOTE_FRAGMENT_TYPE_EXTRA,MainActivity.FragmentType.EDIT);
                break;
        }
        startActivity(intent);
    }
}

/*
  *Boiler plate code by studio
  * public MainActivityListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }
  *
  *
 */

        /*String[] values = new String[] {"Note1","Note2","Note3","Note4","Note5","Note6","Note7"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,values);

        setListAdapter(adapter);
        */