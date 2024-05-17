package ra.service;

import ra.exception.CustomException;
import ra.model.dto.request.ListStudentChoice;
import ra.model.entity.Result;
import ra.model.entity.Test;
import ra.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface ResultService {
    //* Check cau tra loi cua sinh vien va may cham diem
    Result checkAndResultTest(ListStudentChoice listStudentChoice, Long testId) throws CustomException;
    //* học sinh xem lịch sử điểm các bài đã làm
    List<Result> getAllByStudent();
    List<Result> findAllByUserAndTest(User user, Test test);
    Optional<Result> getById(Long id);
    Result save(Result result);
    void hardDeleteById(Long id);
    void softDeleteById(Long id) throws CustomException;
}
