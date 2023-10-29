package com.wanted.feed.user.validation;

import com.wanted.feed.user.validation.ValidationGroups.NotEmptyGroup;
import com.wanted.feed.user.validation.ValidationGroups.PatternCheckGroup;
import com.wanted.feed.user.validation.ValidationGroups.SimilarCheckGroup;
import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

@GroupSequence({Default.class, NotEmptyGroup.class, PatternCheckGroup.class, SimilarCheckGroup.class})
public interface ValidationSequence {
}
