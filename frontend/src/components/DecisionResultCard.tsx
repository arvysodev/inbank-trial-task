import type { LoanDecisionResponse } from "../types/loan";

type DecisionResultCardProps = {
  result: LoanDecisionResponse;
};

export function DecisionResultCard({ result }: DecisionResultCardProps) {
  const isPositive = result.decision === "POSITIVE";

  return (
    <section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
      <h2 className="text-xl font-semibold text-slate-900">Decision result</h2>

      <div className="mt-4 space-y-3 text-sm text-slate-700">
        <p>
          <span className="font-medium text-slate-900">Decision:</span>{" "}
          <span
            className={
              isPositive
                ? "font-semibold text-emerald-700"
                : "font-semibold text-rose-700"
            }
          >
            {result.decision}
          </span>
        </p>

        <p>
          <span className="font-medium text-slate-900">Requested amount:</span>{" "}
          {result.requestedAmount} €
        </p>

        <p>
          <span className="font-medium text-slate-900">Requested period:</span>{" "}
          {result.requestedPeriodMonths} months
        </p>

        {isPositive ? (
          <>
            <p>
              <span className="font-medium text-slate-900">
                Approved amount:
              </span>{" "}
              {result.approvedAmount} €
            </p>
            <p>
              <span className="font-medium text-slate-900">
                Approved period:
              </span>{" "}
              {result.approvedPeriodMonths} months
            </p>
          </>
        ) : (
          <p className="text-rose-700">
            Unfortunately, no suitable loan offer could be found for this
            application.
          </p>
        )}
      </div>
    </section>
  );
}
