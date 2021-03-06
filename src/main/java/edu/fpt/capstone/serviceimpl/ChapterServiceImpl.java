package edu.fpt.capstone.serviceimpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.fpt.capstone.entity.Chapter;
import edu.fpt.capstone.entity.Subject;
import edu.fpt.capstone.entity.Grade;
import edu.fpt.capstone.entity.Lesson;
import edu.fpt.capstone.entity.Version;
import edu.fpt.capstone.repository.ChapterRepository;
import edu.fpt.capstone.repository.SubjectRepository;
import edu.fpt.capstone.repository.GradeRepository;
import edu.fpt.capstone.service.ChapterService;
import edu.fpt.capstone.service.LessonService;
import edu.fpt.capstone.service.MathFormulasAdminService;
import edu.fpt.capstone.service.VersionService;
import edu.fpt.capstone.utils.WebAdminUtils;

@Service
public class ChapterServiceImpl implements ChapterService {
	
	@Autowired
	MathFormulasAdminService service;
	
	@Autowired
	ChapterRepository chapterRepository;
	
	@Autowired
	VersionService versionService;
	
	@Autowired
	LessonService lessonService;
	
	@Autowired
	SubjectRepository subjectRepository;
	
	@Autowired
	GradeRepository gradeRepository;

	@Override
	public List<Chapter> getAllChapters() {
		Iterator<Chapter> cIterator = chapterRepository.findAll().iterator();
		List<Chapter> chapters = new ArrayList<Chapter>();
		for (Chapter chapter; cIterator.hasNext();) {
			chapter = cIterator.next();
			chapters.add(chapter);
		}
		return chapters;
	}
	
	
	@Override
	public List<Chapter> getChapterTreeData() {
		List<Chapter> chapters = getAllChapters();
		int index = 0;
		for (Chapter chapter : chapters) {
			chapter.setChapterIcon(null);
			chapters.set(index++, chapter);
		}
		return chapters;
	}
	
	@Override
	public List<Chapter> getChaptersBySubjectAndGrade(int subjectId, int gradeId) {
		Subject subject = subjectRepository.findOne(subjectId);
		Grade grade = gradeRepository.findOne(gradeId);
		return chapterRepository.findBySubjectIdAndGradeId(subject, grade);
	}

	@Override
	public Chapter getChapterById(int id) {
		return chapterRepository.findOne(id);
	}

	@Override
	public boolean isEqualChapter(Chapter oldChapter, Chapter newChapter) {
		return (oldChapter.getChapterName().equals(newChapter.getChapterName().trim()))
				&& (oldChapter.getSubjectId().getId() == newChapter.getSubjectId().getId())
				&& (oldChapter.getGradeId().getId() == newChapter.getGradeId().getId());
	}

	@Override
	public boolean saveChapters(List<Chapter> chapters) {
		boolean isSuccess = true;
		for (Chapter newChapter : chapters) {
			Version noneVersion = versionService.getVersionById(0);
			if (newChapter.getId() != 0) {
				Chapter oldChapter = getChapterById(newChapter.getId());
				if (!isEqualChapter(oldChapter, newChapter)) {
					if(!isDuplicateChapter(newChapter)) {
						newChapter.setChapterIcon(oldChapter.getChapterIcon());
						newChapter.setVersionId(noneVersion);
						chapterRepository.save(newChapter);
					}
				}
			} else {
				if(!isDuplicateChapter(newChapter)) {
					newChapter.setVersionId(noneVersion);
					chapterRepository.save(newChapter);
				} else {
					isSuccess = false;
				}
			}
		}
		return isSuccess;
	}
	
	@Override
	public boolean isDuplicateChapter(Chapter chapter) {
		List<Chapter> chapters = chapterRepository.getByChapterName(chapter.getChapterName());
		if(chapters != null && !chapters.isEmpty()) {
			for(Chapter c : chapters) {
				if(isEqualChapter(chapter, c)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void deleteChapter(int chapterId) {
		Chapter chapter = getChapterById(chapterId);
		List<Lesson> lessons = lessonService.getLessonsByChapter(chapter);
		for (Lesson l : lessons) {
			lessonService.deleteLesson(l.getId());
		}
		chapterRepository.delete(chapterId);
		service.generateDeleteQuery(chapterId, WebAdminUtils.CHAPTER_TABLE);
	}

	@Override
	public void saveChapter(Chapter chapter) {
		chapterRepository.save(chapter);
	}
	
	@Override
	public List<Chapter> getNewChapters(int userVersion) {
		return chapterRepository.getNewChapters(userVersion);
	}
	
	@Override
	public List<Chapter> getNoneVersionChapters() {
		Version noneVersion = versionService.getVersionById(0);
		return chapterRepository.findByVersionId(noneVersion);
	}
}
