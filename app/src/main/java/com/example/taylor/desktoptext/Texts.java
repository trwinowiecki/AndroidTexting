package com.example.taylor.desktoptext;

import java.util.ArrayList;
import java.util.List;

public class Texts {
    private List<Person> personList = new ArrayList<>();

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    public Texts() {
    }

    public static class Person {
        private String name;
        private String number;
        private List<Message> messageList = new ArrayList<>();

        public Person(String name, String number) {
            this.name = name;
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public List<Message> getMessagesList() {
            return messageList;
        }

        public void setMessagesList(List<Message> messagesList) {
            this.messageList = messagesList;
        }

        public static class Message {
            private String sent;
            private String date;
            private String message;

            public Message(String sent, String date, String message) {
                this.sent = sent;
                this.date = date;
                this.message = message;
            }

            public String getSent() {
                return sent;
            }

            public void setSent(String sent) {
                this.sent = sent;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }
        }
    }
}
