package org.cooltutors.student.ui.services;

// Darren Stults

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import org.cooltutors.student.MainActivity;
import org.cooltutors.student.R;
import org.cooltutors.student.SharedPrefs;

import java.util.List;

public class ServiceRecyclerAdapter extends RecyclerView.Adapter<ServiceRecyclerAdapter.ServiceViewHolder>{

    //region interfaces, declarations, constructors

    // interfaces

    public interface ServiceItemClickListener {
        void onItemClick(Service service, View view);
    }

    // declarations

    private final List<Service> serviceList;
    final private ServiceItemClickListener clickListener;

    // constructors

    public ServiceRecyclerAdapter(List<Service> serviceList, ServiceItemClickListener clickListener) {
        this.serviceList = serviceList;
        this.clickListener = clickListener;
    }

    // endregion

    // internal classes

    class ServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView serviceName;
        private final TextView serviceDescription;
        private final ImageView serviceImage;
        private final TextView serviceInstructor;
        private final LinearLayout instructorHolder;

        public ServiceViewHolder(View view, ServiceRecyclerAdapter adapter) {
            super(view);

            // declarations
            this.serviceName = view.findViewById(R.id.service_name);
            this.serviceDescription = view.findViewById(R.id.service_description);
            this.serviceImage = view.findViewById(R.id.service_image);
            this.serviceInstructor = view.findViewById(R.id.service_instructor);
            this.instructorHolder = view.findViewById(R.id.services_layout_instructors);

            // listener
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Service service = serviceList.get(getAdapterPosition());
            clickListener.onItemClick(service, view);
        }
    }

    //region adapter methods

    @Override
    public ServiceRecyclerAdapter.ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.service_item, viewGroup, false);
        return new ServiceViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceRecyclerAdapter.ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);

        holder.serviceName.setText(service.name);
        holder.serviceDescription.setText(service.description);
        switch (position % 4) { // For until the real images are up:
            case 0:
                holder.serviceImage.setImageResource(R.drawable.ic_money);
                break;
            case 1:
                holder.serviceImage.setImageResource(R.drawable.ic_phone);
                break;
            case 2:
                holder.serviceImage.setImageResource(R.drawable.ic_rebulb);
                break;
            case 3:
                holder.serviceImage.setImageResource(R.drawable.ic_extreme);
                break;
        }
        List<String> instructors = service.getInstructorList();
        TextView newView;
        for (int i = 0; i < instructors.size(); i++) {
            newView = (TextView)LayoutInflater.from(holder.instructorHolder.getContext()).inflate(R.layout.instructor_item, null);
            holder.instructorHolder.addView(newView);
            String instructor = instructors.get(i);
            newView.setText(instructor);
            newView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Toast.makeText(holder.instructorHolder.getContext(), "You clicked on " + instructor + "!", Toast.LENGTH_SHORT).show();
                    SharedPrefs.setSelectedMember(instructor);
                    /*MainActivity.me.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment, MainActivity.me.getFragmentManager().findFragmentById(R.layout.fragment_openings))
                            .commit();*/
                    Navigation.findNavController(view).navigate(R.id.action_navigation_services_to_navigation_openings);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (serviceList == null) return 0;
        return serviceList.size();
    }

    //endregion

}
