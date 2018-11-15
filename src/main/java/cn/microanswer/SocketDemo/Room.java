package cn.microanswer.SocketDemo;

import java.util.ArrayList;

/**
 * 聊天室类。
 * 用户发消息，不应该发给另一个人，用户发的消息应该是发到某个聊天室里。
 * 只要在聊天室里面的人都可以看到发出来的消息。
 */
public class Room {

    // 聊天室名称
    private String name;
    // 聊天室id
    private String id;

    // 自己在聊天室中的引用。
    private Member selfMember;

    // 聊天室中其它的人员。
    private ArrayList<Member> otherMember;

    private RoomListener roomListener;

    Room(String id, String name) {
        this.id = id;
        this.name = name;
        otherMember = new ArrayList<>();
    }

    void dispatchMsg(Msg msg) {
        if (roomListener != null) {
            roomListener.onRoomGetMsg(this, msg);
        }
    }

    public Member getSelfMember() {
        return selfMember;
    }

    public void setSelfMember(Member selfMember) {
        this.selfMember = selfMember;
    }

    public ArrayList<Member> getOtherMember() {
        return otherMember;
    }

    public void addOtherMember(Member member) {
        this.otherMember.add(member);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RoomListener getRoomListener() {
        return roomListener;
    }

    public void setRoomListener(RoomListener roomListener) {
        this.roomListener = roomListener;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Room) {
            Room r = (Room) obj;
            return r.id.equals(this.id);
        }
        return super.equals(obj);
    }

    /**
     * 聊天室成员
     */
    public static class Member {
        public Member(String id, String name) {
            this.id = id;
            this.name = name;
        }

        private String name;
        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Member) {
                Member m = (Member) obj;
                return m.id .equals(this.id);
            }
            return super.equals(obj);
        }
    }


    interface RoomListener {
        void onRoomGetMsg(Room room, Msg msg);
    }
}
