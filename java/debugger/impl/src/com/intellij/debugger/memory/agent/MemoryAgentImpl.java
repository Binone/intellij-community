// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.debugger.memory.agent;

import com.intellij.debugger.engine.evaluation.EvaluateException;
import com.intellij.debugger.engine.evaluation.EvaluationContextImpl;
import com.sun.jdi.ObjectReference;
import org.jetbrains.annotations.NotNull;

import java.util.List;

class MemoryAgentImpl implements MemoryAgent {
  static final MemoryAgent DISABLED = new MemoryAgentImpl(MemoryAgentCapabilities.DISABLED);
  private final MemoryAgentCapabilities myCapabilities;

  MemoryAgentImpl(@NotNull MemoryAgentCapabilities capabilities) {
    myCapabilities = capabilities;
  }

  @Override
  public long estimateObjectSize(@NotNull EvaluationContextImpl evaluationContext, @NotNull ObjectReference reference)
    throws EvaluateException {
    if (!myCapabilities.canEstimateObjectSize()) {
      throw new UnsupportedOperationException("Memory agent can't estimate object size");
    }
    return MemoryAgentOperations.estimateObjectSize(evaluationContext, reference);
  }

  @Override
  public long[] estimateObjectsSizes(@NotNull EvaluationContextImpl evaluationContext, @NotNull List<ObjectReference> references)
    throws EvaluateException {
    if (!myCapabilities.canEstimateObjectsSizes()) {
      throw new UnsupportedOperationException("Memory agent can't estimate objects sizes");
    }

    return MemoryAgentOperations.estimateObjectsSizes(evaluationContext, references);
  }

  @Override
  public @NotNull ReferringObjectsInfo findPathsToClosestGCRoots(@NotNull EvaluationContextImpl evaluationContext,
                                                                 @NotNull ObjectReference reference,
                                                                 int pathsNumber, int objectsNumber) throws EvaluateException {
    if (!myCapabilities.canFindPathsToClosestGcRoots()) {
      throw new UnsupportedOperationException("Memory agent can't provide paths to closest gc roots");
    }

    return MemoryAgentOperations.findPathsToClosestGCRoots(evaluationContext, reference, pathsNumber, objectsNumber);
  }

  @NotNull
  @Override
  public MemoryAgentCapabilities capabilities() {
    return myCapabilities;
  }
}
