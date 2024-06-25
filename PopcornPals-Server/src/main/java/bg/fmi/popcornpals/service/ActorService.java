package bg.fmi.popcornpals.service;

import bg.fmi.popcornpals.dto.ActorDTO;
import bg.fmi.popcornpals.exception.notfound.ActorNotFoundException;
import bg.fmi.popcornpals.dto.ActorRequestDTO;
import bg.fmi.popcornpals.dto.MediaDTO;
import bg.fmi.popcornpals.mapper.ActorMapper;
import bg.fmi.popcornpals.mapper.MediaMapper;
import bg.fmi.popcornpals.model.Actor;
import bg.fmi.popcornpals.model.Media;
import bg.fmi.popcornpals.repository.ActorRepository;
import bg.fmi.popcornpals.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActorService {
    private final ActorRepository actorRepository;
    private final ActorMapper actorMapper;
    private final MediaRepository mediaRepository;
    private final MediaMapper mediaMapper;

    @Autowired
    public ActorService(ActorRepository actorRepository, ActorMapper actorMapper,
                        MediaRepository mediaRepository, MediaMapper mediaMapper) {
        this.actorRepository = actorRepository;
        this.actorMapper = actorMapper;
        this.mediaRepository = mediaRepository;
        this.mediaMapper = mediaMapper;
    }

    public List<ActorDTO> getActors(Integer pageNo, Integer pageSize, String actorName) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Actor> actors = null;
        if(actorName != null) {
            actors = actorRepository.findByNameIgnoreCaseContaining(actorName, pageable);
        }
        else {
            actors = actorRepository.findAll(pageable);
        }
        return actorMapper.toDTOList(actors.getContent());
    }

    public ActorDTO getActorById(Long actorId) {
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(ActorNotFoundException::new);
        return actorMapper.toDTO(actor);
    }

    public ActorDTO createActor(ActorRequestDTO actorRequestDTO) {
        List<Media> starsIn = actorRequestDTO.getStarsIn() != null
                ? mediaRepository.findAllById(actorRequestDTO.getStarsIn())
                : new ArrayList<Media>();

        Actor actor = new Actor();
        actor.setName(actorRequestDTO.getName());
        actor.setDescription(actorRequestDTO.getDescription());
        actor.setBirthdate(actorRequestDTO.getBirthdate());
        actor.setStarsIn(starsIn);

        Actor newActor = actorRepository.save(actor);
        return actorMapper.toDTO(newActor);
    }

    public ActorDTO updateActor(Long actorId, ActorRequestDTO actorRequestDTO) {
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(ActorNotFoundException::new);

        actor.setName(actorRequestDTO.getName());
        actor.setDescription(actorRequestDTO.getDescription());
        actor.setBirthdate(actorRequestDTO.getBirthdate());
        List<Media> starsIn = actorRequestDTO.getStarsIn() != null
                ? mediaRepository.findAllById(actorRequestDTO.getStarsIn())
                : new ArrayList<Media>();
        actor.setStarsIn(starsIn);

        Actor updatedActor = actorRepository.save(actor);
        return actorMapper.toDTO(updatedActor);
    }

    public void deleteActor(Long actorId) {
        Actor toDelete = actorRepository.findById(actorId)
                .orElseThrow(ActorNotFoundException::new);
        actorRepository.delete(toDelete);
    }

    public List<MediaDTO> getMedia(Long actorId) {
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(ActorNotFoundException::new);
        return mediaMapper.toDTOList(actor.getStarsIn());
    }
}
