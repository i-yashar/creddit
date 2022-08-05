package bg.softuni.creddit.service;

import bg.softuni.creddit.model.dto.CreateCommunityDTO;
import bg.softuni.creddit.model.entity.Community;
import bg.softuni.creddit.model.entity.User;
import bg.softuni.creddit.model.view.CommunityView;
import bg.softuni.creddit.repository.CommunityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public CommunityService(CommunityRepository communityRepository, UserService userService, ModelMapper modelMapper) {
        this.communityRepository = communityRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public List<CommunityView> getAllCommunities() {
        return this.communityRepository
                .findAll()
                .stream()
                .map(c -> modelMapper.map(c, CommunityView.class))
                .collect(Collectors.toList());
    }

    public CommunityView getCommunity(String communityName) {
        Community community = this.getCommunityByName(communityName);

        CommunityView communityView = modelMapper.map(community, CommunityView.class);

        communityView.setHasCurrentUserJoined(this.hasCurrentUserJoinedCommunity(communityName));

        System.out.println(communityView.isHasCurrentUserJoined());

        return communityView;
    }

    public boolean hasCurrentUserJoinedCommunity(String communityName) {
        return this.getCommunityByName(communityName)
                .getMembers()
                .stream()
                .map(User::getId)
                .anyMatch(id -> Objects.equals(id, this.userService.getCurrentUser().getId()));
    }

    @Transactional
    public void addUser(String username, String communityName) {
        User user = this.userService.getUserByUsername(username);
        Community community = this.getCommunityByName(communityName);

        Set<User> allCommunityMembers = community.getMembers();

        if(allCommunityMembers == null) {
            allCommunityMembers = new HashSet<>();
        }

        if(allCommunityMembers.contains(user)) {
            throw new IllegalArgumentException("User already part of community");
        }

        allCommunityMembers.add(user);

        this.communityRepository.save(community);
    }

    @Transactional
    public void removeUser(String username, String communityName) {
        User user = this.userService.getUserByUsername(username);
        Community community = this.getCommunityByName(communityName);

        Set<User> allCommunityMembers = community.getMembers();

        if(allCommunityMembers == null) {
            allCommunityMembers = new HashSet<>();
        }

        if(!allCommunityMembers.contains(user)) {
            throw new IllegalArgumentException("User not part of community");
        }

        allCommunityMembers.remove(user);

        this.communityRepository.save(community);
    }

    public void createCommunity(CreateCommunityDTO createCommunityDTO, String creator) {
        Community community = this.modelMapper.map(createCommunityDTO, Community.class);
        community.setMembers(new HashSet<>());
        community.setCreatedBy(this.userService.getUserByUsername(creator));
        community.setCreatedOn(LocalDate.now());

        this.communityRepository.save(community);
    }

    protected Community getCommunityByName(String name) {
        return this.communityRepository
                .findByName(name.startsWith("_") ? name : ("_" + name))
                .orElseThrow(() -> new IllegalArgumentException("Community not found"));
    }
}
