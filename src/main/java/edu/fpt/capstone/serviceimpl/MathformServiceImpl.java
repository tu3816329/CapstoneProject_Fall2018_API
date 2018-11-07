package edu.fpt.capstone.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.fpt.capstone.data.MathformTable;
import edu.fpt.capstone.entity.Exercise;
import edu.fpt.capstone.entity.Lesson;
import edu.fpt.capstone.entity.Mathform;
import edu.fpt.capstone.repository.MathFormRepository;
import edu.fpt.capstone.service.ExerciseService;
import edu.fpt.capstone.service.MathformService;

@Service
public class MathformServiceImpl implements MathformService {

	@Autowired
	MathFormRepository mathFormRepository;
	
	@Autowired
	ExerciseService exerciseService;
	
	@Override
	public Mathform getMathformById(int mathformId) {
		return mathFormRepository.findOne(mathformId);
	}

	@Override
	public void saveMathform(Mathform mathform) {
		mathFormRepository.save(mathform);
	}

	@Override
	public void deleteMathform(int mathformId) {
		Mathform mathform = getMathformById(mathformId);
		List<Exercise> exercises = exerciseService.getExercisesByMathform(mathform);
		for (Exercise e : exercises) {
			exerciseService.deleteExercise(e.getId());
		}
		mathFormRepository.delete(mathform);
	}

	@Override
	public List<MathformTable> getMathformTableDataByLesson(Lesson lesson) {
		List<Mathform> mathforms = mathFormRepository.findByLessonId(lesson);
		List<MathformTable> mathFormTables = new ArrayList<MathformTable>();
		for (Mathform mathform : mathforms) {
			int numOfExs = exerciseService.getExercisesByMathform(mathform).size();
			MathformTable data = new MathformTable(mathform.getId(), mathform.getMathformTitle(), numOfExs,
					mathform.getVersionId());
			mathFormTables.add(data);
		}
		return mathFormTables;
	}

	@Override
	public List<Mathform> getMathformByLesson(Lesson lesson) {
		return mathFormRepository.findByLessonId(lesson);
	}
}