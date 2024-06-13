package bg.fmi.popcornpals.service;

import bg.fmi.popcornpals.dto.StudioDTO;
import bg.fmi.popcornpals.exception.StudioNotFoundException;
import bg.fmi.popcornpals.mapper.StudioMapper;
import bg.fmi.popcornpals.model.Studio;
import bg.fmi.popcornpals.repository.StudioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class StudioService {

    @Autowired
    private StudioRepository studioRepository;
    @Autowired
    private StudioMapper studioMapper;


    public StudioDTO createStudio(StudioDTO studioDTO) {
        Studio studio = studioMapper.toEntity(studioDTO);
        studio.setID(null);
        Studio createdStudio = studioRepository.save(studio);
        return studioMapper.toDTO(createdStudio);
    }
    public StudioDTO getStudioById(Long id) {
        Studio studio = studioRepository.findById(id)
                .orElseThrow(StudioNotFoundException::new);
        return studioMapper.toDTO(studio);
    }
    public List<StudioDTO> getStudios(Integer pageNo, Integer pageSize, String studioName) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Studio> studios = null;
        if(studioName != null) {
            studios = studioRepository.findByNameIgnoreCaseContaining(studioName, pageable);
        }
        else {
            studios = studioRepository.findAll(pageable);
        }
        return studioMapper.toDTOList(studios.getContent());
    }

    public StudioDTO updateStudio(Long studioId, StudioDTO studioDTO) {
        Studio studio = studioRepository.findById(studioId)
                .orElseThrow(StudioNotFoundException::new);

        studio = studioMapper.toEntity(studioDTO);

        Studio updatedStudio = studioRepository.save(studio);
        return studioMapper.toDTO(updatedStudio);
    }
    public void deleteStudio(Long id) {
        Studio toDelete = studioRepository.findById(id)
                .orElseThrow(StudioNotFoundException::new);
        studioRepository.delete(toDelete);
    }
}
