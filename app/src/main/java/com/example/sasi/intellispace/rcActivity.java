package com.example.sasi.intellispace;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class rcActivity extends AppCompatActivity
{

    public ArrayList<CardAdapter> itemCardAdapter =new ArrayList<>();

    public ArrayList<CardAdapter> cardAdapters = new ArrayList<>();
    public ItemAdapter itemArrayAdapter;
    ArrayList<String> building_spinner;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Spinner BuildingSpinnner;
    DatabaseReference databaseReference;
    String SelectedBuilding;
    TextView emptyview;

    @Override
    protected void onResume() {
        super.onResume();
        itemArrayAdapter.setOnItemClickListener(new ItemAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Toast.makeText(rcActivity.this, "Card clicked "+BookingAdapter.getAl().get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_activity);
        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        building_spinner = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        BuildingSpinnner = (Spinner)findViewById(R.id.buildingspinner);
        emptyview =(TextView)findViewById(R.id.textView2);
        String caller = getIntent().getExtras().get("caller").toString();
        System.out.println("bowwww"+caller);
        System.out.println("bowwwww"+BookingAdapter.B+BookingAdapter.ST+BookingAdapter.ET+BookingAdapter.RT);


        building_spinner.add("Select Building");
        databaseReference.child("Building").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    building_spinner.add(child.getKey());
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        setCards();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,building_spinner);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        BuildingSpinnner.setAdapter(arrayAdapter);

        if(itemCardAdapter.isEmpty()){
            emptyview.setVisibility(View.VISIBLE);
        }


        new Vacant_Room_Loading_Task().execute();


        //  new Spinner_Item_Task().execute();

        BuildingSpinnner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!BuildingSpinnner.getSelectedItem().toString().equals("Select Building")){
                    itemCardAdapter.clear();
                    BookingAdapter.al.clear();
                    SelectedBuilding = BuildingSpinnner.getSelectedItem().toString();
                    System.out.println("Selected Building : "+SelectedBuilding);
                    new Vacant_Room_Loading_Task().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    //  public class Spinner_Item_Task extends AsyncTask<String, Integer, String> {
//
//        @Override
//        protected String doInBackground(String... strings) {
//            databaseReference.child("Building").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                   for (DataSnapshot child: dataSnapshot.getChildren()){
//                       building_spinner.add(child.getKey());
//                   }
//                }
//
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//            return null;
//        }
//    }
    void setCards(){
        emptyview.setVisibility(View.GONE);
        itemArrayAdapter = new ItemAdapter(R.layout.building_card, itemCardAdapter);
        recyclerView.setAdapter(itemArrayAdapter);
    }
    public class Vacant_Room_Loading_Task extends AsyncTask<String, Integer,String>{

        @Override
        protected String doInBackground(String... strings) {
            System.out.println("building bow"+BookingAdapter.getB());
            itemCardAdapter.clear();
            BookingAdapter.al.clear();

//            databaseReference.child("Building").child(SelectedBuilding).child("Video-Room").addListenerForSingleValueEvent(new ValueEventListener() {
            databaseReference.child("Bookedtimings").child(BookingAdapter.B).child("Audio-Room").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("Bowww building" );
                    for( DataSnapshot child: dataSnapshot.getChildren()){
                        final String floor = child.getKey();
                        String s=databaseReference.child("Bookedtimings").child(BookingAdapter.B).child("Audio-Room").child(child.getKey()).toString();
                        System.out.println("Bowww "+s);
                        databaseReference.child("Bookedtimings").child(BookingAdapter.B).child("Audio-Room").child(child.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                System.out.println("Room Name : "+dataSnapshot.getChildrenCount());
                                String s1=databaseReference.child("Bookedtimings").child(BookingAdapter.B).child("Audio-Room").child(floor).toString();
                                System.out.println("Boww "+s1);
                                databaseReference.child("Bookedtimings").child(BookingAdapter.B).child("Audio-Room").child(floor).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(final DataSnapshot room: dataSnapshot.getChildren()){
                                            String roomname = room.getKey();
                                            System.out.println("Bowwwww "+BookingAdapter.bookdate);
                                            System.out.println("card "+BookingAdapter.B+floor+BookingAdapter.bookdate);
                                            databaseReference.child("Bookedtimings").child(BookingAdapter.B).child("Audio-Room").child(floor).child(roomname).child(BookingAdapter.bookdate).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {


                                                    System.out.println("ysysys"+dataSnapshot.getChildrenCount());
                                                    for(DataSnapshot kiruba:dataSnapshot.getChildren()){
                                                        // DateAdopter da=kiruba.getValue(DateAdopter.class);
                                                        //  System.out.println("stick "+da.getStartdate()+da.getEnddate());

                                                        cardAdapters.add(new CardAdapter(BookingAdapter.B,floor,room.getKey()));
                                                       // itemCardAdapter.add(new CardAdapter(BookingAdapter.B,floor,room.getKey()));
                                                      //  setCards();

//                                                        BookingAdapter.al.add(new CardAdapter(BookingAdapter.B,floor,room.getKey()));
                                                    }
                                                    new Checking_Room_Task().execute();


//

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });



                                        }
                                        System.out.println("Cards : "+itemCardAdapter.size());
//                                        setCards();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            return null;
        }
    }

    public class Checking_Room_Task extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {

//            databaseReference.child("Building").child(SelectedBuilding).child("Video-Room").addListenerForSingleValueEvent(new ValueEventListener() {
            databaseReference.child("Building").child(BookingAdapter.B).child("Audio-Room").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("Bowww building" );
                    for( DataSnapshot child: dataSnapshot.getChildren()){
                        final String floor = child.getKey();

                        databaseReference.child("Building").child(BookingAdapter.B).child("Audio-Room").child(child.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                System.out.println("Room Name : "+dataSnapshot.getChildrenCount());

                                databaseReference.child("Building").child(BookingAdapter.B).child("Audio-Room").child(floor).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(final DataSnapshot room: dataSnapshot.getChildren()){
                                            String roomname = room.getKey();
                                            System.out.println("Bowwwww "+BookingAdapter.bookdate);
                                            System.out.println("card "+BookingAdapter.B+floor+BookingAdapter.bookdate);
                                            System.out.println("ysysys"+dataSnapshot.getChildrenCount());

                                            CardAdapter cardAdapter = new CardAdapter(BookingAdapter.B,floor,room.getKey());

                                            BookingAdapter.al.add(cardAdapter);
                                            System.out.println("Card adapters : "+cardAdapters);

                                            if(!cardAdapters.contains(cardAdapter)){
                                                itemCardAdapter.add(new CardAdapter(BookingAdapter.B,floor,room.getKey()));
                                            }



                                        }
                                        System.out.println("Cards : "+itemCardAdapter.size());

                                        setCards();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }
    }
}