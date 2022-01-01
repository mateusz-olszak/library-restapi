package com.library.service.facade;

import com.library.domain.Copy;
import com.library.dto.books.CopyDto;
import com.library.mappers.CopyMapper;
import com.library.service.CopyService;
import com.library.status.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CopyFacade {

    private final CopyService copyService;
    private final CopyMapper copyMapper;

    public List<CopyDto> getAllCopies() {
        List<Copy> copies = copyService.findAllCopies();
        return copyMapper.mapToListCopyDto(copies);
    }

    public List<CopyDto> getAllCopiesForGivenBookId(int bookId) {
        List<Copy> copies = copyService.retrieveAvailableCopiesForGivenId(bookId);
        return copyMapper.mapToListCopyDto(copies);
    }

    public CopyDto getCopy(int copyId) {
        Copy copy = copyService.findCopy(copyId);
        return copyMapper.mapToCopyDto(copy);
    }

    public void saveCopy(CopyDto copyDto) {
        Copy copy = copyMapper.mapToCopy(copyDto);
        copyService.saveCopy(copy);
    }

    public CopyDto changeCopyStatus(int copyId, Status status) {
        return copyMapper.mapToCopyDto(copyService.changeCopyStatus(copyId, status));
    }

    public void deleteCopy(int copyId) {
        copyService.deleteCopy(copyId);
    }
}
