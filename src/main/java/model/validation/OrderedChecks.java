package model.validation;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({Default.class, Extended.class})
public interface OrderedChecks {
}
