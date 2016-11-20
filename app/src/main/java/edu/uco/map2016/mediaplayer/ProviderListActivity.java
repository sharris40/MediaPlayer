package edu.uco.map2016.mediaplayer;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;

import edu.uco.map2016.mediaplayer.services.ProviderRegistry;
import edu.uco.map2016.mediaplayer.services.ProviderService;

public class ProviderListActivity extends ListActivity {
    private class ProviderAdapter extends ArrayAdapter<ProviderService> {
        public ProviderAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2);
        }

        @Override
        public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
            ProviderService service = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            }
            TextView name = (TextView)convertView.findViewById(android.R.id.text1);
            TextView status = (TextView)convertView.findViewById(android.R.id.text2);
            if (service != null) {
                name.setText(service.getProviderName());
                if (service.isConnected()) {
                    status.setText(R.string.actProviderList_status_connected);
                } else {
                    status.setText(R.string.actProviderList_status_empty);
                }
            }
            return convertView;
        }
    }

    private ProviderAdapter mAdapter;
    private final LinkedList<ServiceConnection> mConnections = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ProviderAdapter(this);
        setListAdapter(mAdapter);
        for (Class<? extends ProviderService> service : ProviderRegistry.getInstance().getServices()) {
            Intent startIntent = new Intent(this, service);
              bindService(startIntent, new ServiceConnection() {
                private ProviderService mService;
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    mConnections.add(this);
                    ProviderService.ProviderBinder binder = (ProviderService.ProviderBinder) service;
                    mService = binder.getService();
                    mAdapter.add(mService);
                    mService.addListener(new ProviderService.ProviderListener() {
                        @Override
                        public void onAsynchronousOperationProgress(int requestType, int requestCode, int responseCode, float progress) {

                        }

                        @Override
                        public void onAsynchronousOperationComplete(int requestType, int requestCode, int responseCode) {

                        }

                        @Override
                        public void onMessage(int messageCode, int responseCode) {
                            if (messageCode == ProviderService.MESSAGE_CONNECTION_UPDATE) {
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    mAdapter.remove(mService);
                    mConnections.remove(this);
                }
            }, 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (ServiceConnection connection : mConnections) {
            unbindService(connection);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        ProviderService service = (ProviderService)list.getItemAtPosition(position);
        if (!service.isConnected()) {
            Intent intent = service.createConnectionActivity(this);
            startActivity(intent);
        }
    }
}
