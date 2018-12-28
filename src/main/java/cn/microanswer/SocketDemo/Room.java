package cn.microanswer.SocketDemo;

import java.util.ArrayList;

/**
 * 聊天室类。
 * 用户发消息，不应该发给另一个人，用户发的消息应该是发到某个聊天室里。
 * 只要在聊天室里面的人都可以看到发出来的消息。 这样，方便实现群聊功能。
 */
public class Room {

    // 聊天室名称
    private String name;
    // 聊天室id
    private String id;
    // 是否一对一聊天室。
    private boolean isSignle;

    // 聊天室中其它的人员。
    private ArrayList<Member> members;

    @com.alibaba.fastjson.annotation.JSONField(deserialize = false, serialize = false)
    private RoomListener roomListener;

    Room() {
        members = new ArrayList<>();
    }

    Room(String id, String name) {
        this.id = id;
        this.name = name;
        members = new ArrayList<>();
    }

    void dispatchMsg(Msg msg) {
        if (roomListener != null) {
            roomListener.onRoomGetMsg(this, msg);
        }
    }

    public boolean isSignle() {
        return isSignle;
    }

    public void setSignle(boolean signle) {
        isSignle = signle;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public void addMember(Member member) {
        this.members.add(member);
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
    public boolean equals(Object obj) { // 成员相同，则认为房间相同。
        if (obj instanceof Room) {
            Room r = (Room) obj;
            ArrayList<Member> members = r.getMembers();
            for (Member member : members) {
                if (!getMembers().contains(member)) {
                    return false;
                }
            }
            return members.size() == getMembers().size();
        }
        return super.equals(obj);
    }

    public void memberReNamed(String fromClientId, String newName) {
        for (Member member : members) {
            if (member.getId().equals(fromClientId)) {
                String name = member.getName();
                member.setName(newName);
                if (roomListener != null) roomListener.onMemberReNamed(member, name);
                return;
            }
        }
    }

    public void onMemberOffLine(Member member) {
        for (int i = 0; i < members.size(); i++) {
            if (member.getId().equals(members.get(i).getId())) {
                members.remove(i);
                if (members.size() <= 1) {
                    if (roomListener != null) roomListener.onRoomOff(this);
                }
                return;
            }
        }
    }

    /**
     * 聊天室成员
     */
    public static class Member implements Comparable<Member> {
        public Member(String id, String name) {
            this.id = id;
            this.name = name;
        }

        private String name;
        private String id;

        public Member() {
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

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Member) {
                Member m = (Member) obj;
                return m.id.equals(this.id);
            }
            return super.equals(obj);
        }

        @Override
        public String toString() {
            return this.id;
        }

        @Override
        public int compareTo(Member o) {
            return id.compareTo(o.getId());
        }
    }


    interface RoomListener {
        void onRoomGetMsg(Room room, Msg msg);

        void onMemberReNamed(Member member, String oldName);

        void onRoomOff(Room room); // 当成员下线到只有一个人时，会自动关闭房间，此方法调起。
    }
}
