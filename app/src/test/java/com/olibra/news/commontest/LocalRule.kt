package com.olibra.news.commontest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class LocalTestRule : TestRule {
    override fun apply(base: Statement?, description: Description?): Statement {
        return RuleChain
            .outerRule(InstantTaskExecutorRule())
            .around(RxTestRule())
            .apply(base, description)
    }
}