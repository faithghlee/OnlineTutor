package com.asanka.tutor.service.impl;

import com.asanka.tutor.domain.Authority;
import com.asanka.tutor.domain.User;
import com.asanka.tutor.domain.Video;
import com.asanka.tutor.repository.UserRepository;
import com.asanka.tutor.repository.VideoRepository;
import com.asanka.tutor.security.AuthoritiesConstants;
import com.asanka.tutor.security.UserNotLoggedInException;
import com.asanka.tutor.service.UserService;
import com.asanka.tutor.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing {@link Video}.
 */
@Service
public class VideoServiceImpl implements VideoService {

    private final Logger log = LoggerFactory.getLogger(VideoServiceImpl.class);

    private final VideoRepository videoRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    public VideoServiceImpl(VideoRepository videoRepository, UserRepository userRepository, UserService userService) {
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * Save a video.
     *
     * @param video the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Video save(Video video) {
        log.debug("Request to save Video : {}", video);
        return videoRepository.save(video);
    }

    /**
     * Get all the videos.
     *
     * @return the list of entities.
     */
    @Override
    public List<Video> findAll() {
        log.debug("Request to get all Videos");
        return videoRepository.findAll();
    }

    /**
     * Get all the videos for a collection of chapters.
     *
     * @return the list of entities
     */
    @Override
    public List<Video> findAllVideosForChapters(String[] chapterIDs) {
        log.debug("Request to get all Videos for the chapters");
        List<Video> videos = videoRepository.findVideosByChapterIDIn(chapterIDs);
        Optional<User> user = userService.getUserWithAuthorities();
        Authority adminAuthority = new Authority();
        adminAuthority.setName(AuthoritiesConstants.ADMIN);
        if (!user.isPresent()) {
            for (Video video: videos) {
                if (!video.isIsSample())
                    video.setUrl(null);
            }
        } else if (!user.get().getAuthorities().contains(adminAuthority)) {
            Set<String> chapters = user.get().getChapters();
            if (chapters == null) {
                for (Video video: videos) {
                    if (!video.isIsSample())
                        video.setUrl(null);
                }
            } else {
                for (Video video : videos) {
                    if (!chapters.contains(video.getChapterID()) && !video.isIsSample())
                        video.setUrl(null);
                }
            }
        }
        return videos;
    }

    /**
     * Get one video by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<Video> findOne(String id) {
        log.debug("Request to get Video : {}", id);
        return videoRepository.findById(id);
    }

    /**
     * Delete the video by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Video : {}", id);
        videoRepository.deleteById(id);
    }
}
