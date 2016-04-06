package com.valyakinaleksey.amocrm.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.valyakinaleksey.amocrm.R;
import com.valyakinaleksey.amocrm.domain.LeadsAdapter;
import com.valyakinaleksey.amocrm.domain.ServiceGenerator;
import com.valyakinaleksey.amocrm.models.MyLead;
import com.valyakinaleksey.amocrm.models.api.APIError;
import com.valyakinaleksey.amocrm.models.api.Lead;
import com.valyakinaleksey.amocrm.models.api.LeadsResponse;
import com.valyakinaleksey.amocrm.models.api.LeadsStatusesResponse;
import com.valyakinaleksey.amocrm.models.api.Response;
import com.valyakinaleksey.amocrm.util.ErrorUtils;
import com.valyakinaleksey.amocrm.util.LeadUtils;
import com.valyakinaleksey.amocrm.util.Logger;
import com.valyakinaleksey.amocrm.util.Session;
import com.valyakinaleksey.amocrm.util.ToastUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class LeadsFragment extends Fragment {
    private LeadsAdapter leadsAdapter;
    private List<MyLead> leadList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        leadsAdapter = new LeadsAdapter(leadList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leads, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_leads);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(leadsAdapter);
        view.findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.clearSession(getContext());
                navigateToAuth();
            }
        });
        return view;
    }

    private void navigateToAuth() {
        ((Navigator) getActivity()).navigateToFragment(new LoginFragment());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ServiceGenerator.AmoCrmApiInterface service = ServiceGenerator.createService(ServiceGenerator.AmoCrmApiInterface.class);
        if (!TextUtils.isEmpty(Session.SESSION_ID)) {
            if (LeadUtils.isLeadStatusesEmpty()) {
                initLeadsStatuses(service);
            } else {
                initLeads(service);
            }
        } else {
            navigateToAuth();
        }
    }

    private void initLeads(ServiceGenerator.AmoCrmApiInterface service) {
        Call<Response<LeadsResponse>> leadsResponse = service.getLeads();
        leadsResponse.enqueue(new Callback<Response<LeadsResponse>>() {
            @Override
            public void onResponse(Call<Response<LeadsResponse>> call, retrofit2.Response<Response<LeadsResponse>> response) {
                if (response.isSuccessful()) {
                    List<Lead> leads = response.body().response.leads;
                    leadList.clear();
                    leadList.addAll(LeadUtils.convertLeads(leads));
                    leadsAdapter.notifyDataSetChanged();
                } else {
                    APIError apiError = ErrorUtils.parseError(response);
                    if (!TextUtils.isEmpty(apiError.error)) {
                        ToastUtils.showShortMessage(apiError.error, getContext());
                        switch (apiError.error_code) {
                            //и т д. здесь можно обрабатывать наши ошибки
                            case ErrorUtils.ERROR_LEAD_UPDATE_EMPTY_ARRAY:
                                break;
                        }
                        Logger.d(apiError.error);
                        Logger.d(String.valueOf(apiError.error_code));
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<LeadsResponse>> call, Throwable t) {
                Logger.d(t.toString());
                ToastUtils.showShortMessage("Check your internet connection", getContext());
            }
        });
    }

    private void initLeadsStatuses(final ServiceGenerator.AmoCrmApiInterface service) {
        Call<Response<LeadsStatusesResponse>> leadsStatusesResponse = service.getLeadStatuses();
        leadsStatusesResponse.enqueue(new Callback<Response<LeadsStatusesResponse>>() {
            @Override
            public void onResponse(Call<Response<LeadsStatusesResponse>> call, retrofit2.Response<Response<LeadsStatusesResponse>> response) {
                if (response.isSuccessful()) {
                    LeadUtils.setLeadStatuses(response.body().response.account.leadStatuses);
                    initLeads(service);
                } else {
                    try {
                        Logger.d(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<LeadsStatusesResponse>> call, Throwable t) {

            }
        });
    }
}
