package com.android.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

//Copyright Carlos Mbendera

public class MainActivity extends AppCompatActivity {


    private final List<FeedRow> listRowsDone = new ArrayList<>();
    private final List<FeedRow> listRowsToDo = new ArrayList<>();
    private final List<FeedRow> listRowsDoing = new ArrayList<>();
    private final List<FeedRow> listRowsOutput = new ArrayList<>();
    String currentList;
    private TextView mTextMessage;
    private FeedListViewAdapter listViewAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView = findViewById(R.id.mainList);
        currentList = "To Do";
        listViewAdapter = new FeedListViewAdapter(getApplication(), listRowsOutput);
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (currentList.matches("Doing")) {

                    Toast.makeText(MainActivity.this, "You've marked the" + listRowsDoing.get(position).getTask() + " as done.", Toast.LENGTH_SHORT).show();
                    listRowsDone.add(listRowsDoing.get(position));
                    listRowsDoing.remove(position);

                    listRowsOutput.clear();
                    listRowsOutput.addAll(listRowsDoing);
                    listViewAdapter.notifyDataSetChanged();

                } else if (currentList.matches("To Do")) {

                    Toast.makeText(MainActivity.this, "You've started " + listRowsToDo.get(position).getTask() + ".", Toast.LENGTH_SHORT).show();
                    listRowsDoing.add(listRowsToDo.get(position));
                    listRowsToDo.remove(position);

                    listRowsOutput.clear();
                    listRowsOutput.addAll(listRowsToDo);
                    listViewAdapter.notifyDataSetChanged();

                }

            }
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(" What Do You Need To DO?");
                final EditText editText = new EditText(getApplicationContext());
                editText.setHint("Task Comes Here");
                builder.setView(editText);

                builder.setPositiveButton("That's it.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.d("Task", editText.getText().toString());
                        listRowsToDo.add(new FeedRow(
                                editText.getText().toString()

                        ));

                        if (currentList.matches("To Do")) {

                            listRowsOutput.clear();
                            listRowsOutput.addAll(listRowsToDo);
                            listViewAdapter.notifyDataSetChanged();

                        }

                        Toast.makeText(MainActivity.this, "Noted", Toast.LENGTH_SHORT).show();


                    }
                });

                builder.setNegativeButton("Never Mind.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_doing:
                    currentList = "Doing";
                    listRowsOutput.clear();
                    listRowsOutput.addAll(listRowsDoing);
                    listViewAdapter.notifyDataSetChanged();
                    return true;
                case R.id.navigation_to_do:

                    currentList = "To Do";
                    listRowsOutput.clear();
                    listRowsOutput.addAll(listRowsToDo);
                    listViewAdapter.notifyDataSetChanged();
                    return true;
                case R.id.navigation_done:

                    currentList = "Done";
                    listRowsOutput.clear();
                    listRowsOutput.addAll(listRowsDone);
                    listViewAdapter.notifyDataSetChanged();
                    return true;
            }
            return false;
        }
    };


    class FeedRow {


        private String task;


        FeedRow(String rowTask) {


            task = rowTask;


        }


        String getTask() {
            return task;
        }

        public void setTask(String task) {
            this.task = task;
        }


    }

    class FeedListViewAdapter extends BaseAdapter {

        private final Context context;
        private final List<FeedRow> feedRowList;

        FeedListViewAdapter(Context context, List<FeedRow> feedRowList) {

            this.context = context;
            this.feedRowList = feedRowList;
        }

        @Override
        public int getCount() {
            return feedRowList.size();
        }

        @Override
        public Object getItem(int position) {
            return feedRowList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            FeedListViewAdapter.ViewHolder viewHolder;

            if (convertView == null) {

                convertView = LayoutInflater.from(context).inflate(R.layout.task_item, null);

                viewHolder = new FeedListViewAdapter.ViewHolder();

                viewHolder.task = convertView.findViewById(R.id.task);


                convertView.setTag(viewHolder);

            } else {
                viewHolder = (FeedListViewAdapter.ViewHolder) convertView.getTag();

            }
            FeedRow feedRow = feedRowList.get(position);


            viewHolder.task.setText(feedRow.getTask());


            return convertView;
        }

        class ViewHolder {

            TextView task;


        }
    }
}
