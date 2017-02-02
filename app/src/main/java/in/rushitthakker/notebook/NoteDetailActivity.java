package in.rushitthakker.notebook;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NoteDetailActivity extends AppCompatActivity {
    public static final String NEW_NOTE_EXTRA = "New Note";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        createAndAddFragment();
    }

    private void createAndAddFragment(){
        //Setting tittle


        //Getting the fragment type
        MainActivity.FragmentType fragmentType = (MainActivity.FragmentType) getIntent().getSerializableExtra(MainActivity.NOTE_FRAGMENT_TYPE_EXTRA);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (fragmentType){
            case EDIT:
                NoteEditFragment noteEditFragment = new NoteEditFragment();
                setTitle(R.string.editFragmentTitle);
                fragmentTransaction.add(R.id.activity_note_detail,noteEditFragment,"NOTE_EDIT_FRAGMENT");
                break;
            case VIEW:
                NoteViewFragment noteViewFragment = new NoteViewFragment();
                setTitle(R.string.viewFragmentTitle);
                fragmentTransaction.add(R.id.activity_note_detail,noteViewFragment,"NOTE_VIEW_FRAGMENT");
                break;
            case CREATE:
                NoteEditFragment noteNewFragment = new NoteEditFragment();
                setTitle(R.string.newFragmentTitle);

                Bundle bundle = new Bundle();
                bundle.putBoolean(NEW_NOTE_EXTRA,true);
                noteNewFragment.setArguments(bundle);
                fragmentTransaction.add(R.id.activity_note_detail,noteNewFragment,"NOTE_NEW_FRAGMENT");
                break;
        }



        fragmentTransaction.commit();
    }
}
