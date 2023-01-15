package com.example.topgoback.Tools;

import com.example.topgoback.Messages.Model.Message;

import java.util.Comparator;

public class MessageTimeComparator implements Comparator<Message> {
    @Override
    public int compare(Message m1, Message m2) {
        return m1.getTimeOfSending().compareTo(m2.getTimeOfSending());
    }
}