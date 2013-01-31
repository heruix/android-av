package com.steelthorn.android.av;

import java.util.Arrays;
import java.util.List;

class DefaultScanEngine extends ScanEngine
{

	public ScanResult scan(ScanContext ctx)
	{
		ScanResult result = new ScanResult();

		for (ITargetSource source : ctx.getSources())
		{
			if (ctx.getListener() != null)
				ctx.getListener().onTargetSourceSwitch(source);

			for (IScanTarget target : source)
			{
				if (ctx.getListener() != null)
					ctx.getListener().onTargetScanBegin(target);

				ThreatInfo ti = scanTarget(target);

				if (ti != null)
					result.addMatchFound(ti);

				if (ctx.getListener() != null)
					ctx.getListener().onTargetScanComplete(target, ti);
			}
		}

		return result;
	}

	public ThreatInfo scanTarget(IScanTarget target)
	{
		
		// O(n) scanning algorithm for now
		// TODO: BST engine based on size
		List<IScanDefinition> defs = Util.getDevDefinitions();

		for (IScanDefinition def : defs)
		{
			if ((def.getDefinitionType() == target.getTargetType()) && Arrays.equals(def.getHashValue(), target.getHashValue()))
				return new ThreatInfo(target, def);
		}

		return null;
	}
}
