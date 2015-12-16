package com.canchala.julio.elorauser;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Firebase mRef;
    ListView listView ;
    TextView let;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        Firebase.setAndroidContext(this);
        listView = (ListView) findViewById(R.id.list);
        mRef = new Firebase("https://eloraserver.firebaseio.com/");
        let=(TextView)findViewById(R.id.letre);

        Firebase productos = mRef.child("Productos");

        Firebase estado = mRef.child("Estado");

        estado.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String es = (String) dataSnapshot.getValue();

                if(es.equals("Cerrado"))
                {
                    listView.setVisibility(View.INVISIBLE);
                    let.setVisibility(View.VISIBLE);
                }
                if(es.equals("Abierto"))
                {
                    listView.setVisibility(View.VISIBLE);
                    let.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        productos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                int contador = (int) snapshot.getChildrenCount();
                int i = 0;
                String[] produc = new String[contador];
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    mercancia merc = postSnapshot.getValue(mercancia.class);
                    produc[i] = merc.getNombre();
                    i++;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, produc);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(MainActivity.this, "Lectura fallida", Toast.LENGTH_SHORT).show();
            }
        });


        Query query = productos;
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String info = (String) dataSnapshot.child("nombre").getValue();
                Toast.makeText(MainActivity.this, "El Producto " + info + " cambio", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String info = (String) dataSnapshot.child("nombre").getValue();
                Toast.makeText(MainActivity.this, "El Producto " + info + " fue borrado", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }


        });
    }
}
