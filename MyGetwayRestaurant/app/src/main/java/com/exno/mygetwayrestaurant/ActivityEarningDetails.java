package com.exno.mygetwayrestaurant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

public class ActivityEarningDetails extends AppCompatActivity {
    RecyclerView recyclerView_payment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earning_details);
        /*List<Paymentdetails> data = response.body().getData();
        recyclerView_payment = (RecyclerView) findViewById(R.id.recyclerView_payment);
        MyListAdapter_bed adapter_bed = new MyListAdapter_bed(data);
        recyclerView_payment.setHasFixedSize(true);
        recyclerView_payment.setLayoutManager(new LinearLayoutManager(ActivityActivity.this));
        recyclerView_payment.setAdapter(adapter_bed);
        recyclerView_payment.scrollToPosition(0);*/
    }

    /*public class MyListAdapter_bed extends RecyclerView.Adapter<MyListAdapter_bed.ViewHolder>{
        //ArrayList<MyListData_bed>  listdata;
        List<Paymentdetails>   listdata;

        // RecyclerView recyclerView;
        *//*public MyListAdapter_bed(ArrayList<MyListData_bed>  listdata)
        {
            this.listdata = listdata;
        }*//*
        public MyListAdapter_bed(List<Paymentdetails>   listdata)
        {
            this.listdata = listdata;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.list_item_payment, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position)
        {


            holder.s_date.setText(listdata.get(position).getDoc());
            holder.s_porposr.setText(listdata.get(position).getPurpose());
            holder.s_method.setText(listdata.get(position).getMode());
            holder.s_trans_id.setText(listdata.get(position).getTransaction_id());
            try
            {
                tenant_name.setText(listdata.get(position).getTenant_name());
            }
            catch (Exception e){

            }

            // holder.imageView.setImageResource(listdata.get(position).getBooking_id());
            *//*holder.relativeLayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    // Toast.makeText(view.getContext(),"click on item: "+myListData.getDescription(),Toast.LENGTH_LONG).show();
                }
            });*//*
        }


        @Override
        public int getItemCount()
        {
            return listdata.size();
        }

        public  class ViewHolder extends RecyclerView.ViewHolder
        {
            public ImageView imageView;
            public TextView bookingno;
            public TextView s_date;
            public TextView s_trans_id;
            public TextView s_porposr,s_method;
            public RelativeLayout relativeLayout;
            public ViewHolder(View itemView)
            {
                super(itemView);
                //  this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
                this.s_date = (TextView) itemView.findViewById(R.id.s_date);
                this.s_porposr = (TextView) itemView.findViewById(R.id.s_porposr);
                this.s_method = (TextView) itemView.findViewById(R.id.s_method);
                this.s_trans_id = (TextView) itemView.findViewById(R.id.s_trans_id);
                relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
            }
        }
    }*/
}
