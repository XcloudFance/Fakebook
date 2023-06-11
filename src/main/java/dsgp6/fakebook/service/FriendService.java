package dsgp6.fakebook.service;

import dsgp6.fakebook.model.User;
import dsgp6.fakebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendService {
    private final UserRepository userRepository;

    @Autowired
    public FriendService(UserRepository userRepository) {this.userRepository = userRepository;}

    public boolean addFriend (String userUID, String friendUID) {
        //find the users
        User sender = userRepository.findByUid(userUID);
        User receiver = userRepository.findByUid(friendUID);
        if (sender == null || receiver == null) {;return false;}
        //add to friend list
        receiver.getFriends().add(sender.getUid());
        sender.getFriends().add(receiver.getUid());
        //update number of friends
        sender.setNumberOfFriends(sender.getNumberOfFriends()+1);
        receiver.setNumberOfFriends(receiver.getNumberOfFriends()+1);

        userRepository.save(receiver);
        userRepository.save(sender);
        return true;
    }
    public boolean removeFriend(String userUID, String friendUID) {
        //find the users
        User user = userRepository.findByUid(userUID);
        User friend = userRepository.findByUid(friendUID);
        if (user == null || friend == null) {return false;}
        if (!user.getFriends().contains(friendUID) || !friend.getFriends().contains(userUID)){
            return false;}
        //remove from friend list
        user.getFriends().remove(friendUID);
        friend.getFriends().remove(userUID);
        //update number of friends
        user.setNumberOfFriends(user.getNumberOfFriends()-1);
        friend.setNumberOfFriends(friend.getNumberOfFriends()-1);

        userRepository.save(user);
        userRepository.save(friend);
        return true;
    }

    public String [] getFriendList (String userUID) {
        User user = userRepository.findByUid(userUID);
        String [] list = new String[user.getNumberOfFriends()];
        if (user!=null){
            for (int i = 0; i<user.getNumberOfFriends(); i++){
                String friendUID = user.getFriends().get(i);
                User friend = userRepository.findByUid(friendUID);
                list[i] = friend.getUsername();
            }
            return list;
        } else {return null;}
    }
}
