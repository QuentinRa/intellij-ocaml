package com.or.ide.insight;

import com.or.lang.utils.OclQNameFinder;

public class OclCompletionContributor extends ORCompletionContributor {
    OclCompletionContributor() {
        super(OclQNameFinder.INSTANCE);
    }
}
