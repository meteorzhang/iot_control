package app.iot.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.iot.R;
import app.iot.model.ProtocolDetail;

public class ProtocolDetailAdapter extends RecyclerView.Adapter<ProtocolDetailAdapter.ViewHolder> {

    private List<ProtocolDetail> detailList;

    public ProtocolDetailAdapter(List<ProtocolDetail> detailList) {
        this.detailList = detailList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_protocol_detail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ProtocolDetail protocolDetail = detailList.get(position);
        holder.name.setText(protocolDetail.getDesc());
        Integer status = protocolDetail.getStatus();
        if(status!=null){
            if(status == 0){
                holder.state.setText("故障");
                holder.state.setTextColor(holder.state.getContext().getColor(R.color.colorRed));
            }else{
                holder.state.setText("正常");
                holder.state.setTextColor(holder.state.getContext().getColor(R.color.textColorGreen));
            }
        }
    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView state;

        public ViewHolder(View view) {
            super(view);
            name =  view.findViewById(R.id.name);
            state =  view.findViewById(R.id.state);
        }

    }
}