package com.iktpreobuka.projekat_za_kraj.util;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TeacherSubjectId implements Serializable {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "teacher")
    private Integer teacherId;
 
    @Column(name = "subject")
    private Integer subjectId;
 
    
    public TeacherSubjectId() {}

	public TeacherSubjectId(Integer teacherId, Integer subjectId) {
        this.teacherId = teacherId;
        this.subjectId = subjectId;
    }
    
    //Getters omitted for brevity
 
	public Integer getTeacher() {
		return teacherId;
	}

	public Integer getSubject() {
		return subjectId;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass())
            return false;
 
        TeacherSubjectId that = (TeacherSubjectId) o;
        return Objects.equals(teacherId, that.teacherId) &&
               Objects.equals(subjectId, that.subjectId);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(teacherId, subjectId);
    }
    
}
