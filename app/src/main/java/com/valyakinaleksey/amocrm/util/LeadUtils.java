package com.valyakinaleksey.amocrm.util;

import com.valyakinaleksey.amocrm.models.MyLead;
import com.valyakinaleksey.amocrm.models.api.Lead;
import com.valyakinaleksey.amocrm.models.api.LeadStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeadUtils {
    private static Map<Integer, LeadStatus> leadStatusMap = new HashMap<>();

    public static MyLead convertLead(Lead lead) {
        MyLead myLead = new MyLead(lead.id);
        myLead.setName(lead.name);
        myLead.setPrice(lead.price);
        LeadStatus leadStatus = leadStatusMap.get(lead.status_id);
        myLead.setStatus(leadStatus == null ? "" : leadStatus.name);
        return myLead;
    }

    public static List<MyLead> convertLeads(List<Lead> leadList) {
        List<MyLead> myLeads = new ArrayList<>();
        for (int i = 0; i < leadList.size(); i++) {
            myLeads.add(convertLead(leadList.get(i)));
        }
        return myLeads;
    }

    public static void setLeadStatuses(List<LeadStatus> leadStatuses) {
        leadStatusMap.clear();
        for (int i = 0; i < leadStatuses.size(); i++) {
            LeadStatus leadStatus = leadStatuses.get(i);
            leadStatusMap.put(leadStatus.id, leadStatus);
        }
    }

    public static boolean isLeadStatusesEmpty() {
        return leadStatusMap.isEmpty();
    }
}
