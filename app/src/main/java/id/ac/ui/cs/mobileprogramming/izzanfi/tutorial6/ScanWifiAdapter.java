package id.ac.ui.cs.mobileprogramming.izzanfi.tutorial6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScanWifiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<WifiFragment.Device> availableWifiList;
    private Context context;
    private View.OnClickListener onClickListener;

    public ScanWifiAdapter(List<WifiFragment.Device> availableWifiList, Context context) {
        this.availableWifiList = availableWifiList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_wifi_ssid,
                parent, false);

        WifiSsidViewHolder viewHolder = new WifiSsidViewHolder(itemView);
        itemView.setTag(viewHolder);
        itemView.setOnClickListener(onClickListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WifiFragment.Device deviceObj = availableWifiList.get(position);
        String ssid = deviceObj.getName().toString();

        ((WifiSsidViewHolder) holder).ssidNameView.setText(ssid);
        ((WifiSsidViewHolder) holder).ssidNameView.setTag(deviceObj);

        ((WifiSsidViewHolder) holder).context = context;
        ((WifiSsidViewHolder) holder).position = position;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return availableWifiList.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public class WifiSsidViewHolder extends RecyclerView.ViewHolder {
        protected TextView ssidNameView;
        protected Context context;
        protected int position;

        public WifiSsidViewHolder(View view) {
            super(view);
            ssidNameView = view.findViewById(R.id.ssid_name);
        }
    }
}
