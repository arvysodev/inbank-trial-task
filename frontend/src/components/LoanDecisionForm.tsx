import { useState } from "react";
import { calculateLoanDecision } from "../api/loanDecisionApi";
import type { LoanDecisionRequest, LoanDecisionResponse } from "../types/loan";

type LoanDecisionFormProps = {
  onResult: (result: LoanDecisionResponse) => void;
};

const initialFormState: LoanDecisionRequest = {
  personalCode: "",
  loanAmount: 2000,
  loanPeriodMonths: 12,
};

export function LoanDecisionForm({ onResult }: LoanDecisionFormProps) {
  const [form, setForm] = useState<LoanDecisionRequest>(initialFormState);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  function handleChange<K extends keyof LoanDecisionRequest>(
    field: K,
    value: LoanDecisionRequest[K],
  ) {
    setForm((current) => ({
      ...current,
      [field]: value,
    }));
  }

  async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setErrorMessage(null);
    setIsSubmitting(true);

    try {
      const result = await calculateLoanDecision(form);
      onResult(result);
    } catch (error) {
      if (error instanceof Error) {
        setErrorMessage(error.message);
      } else {
        setErrorMessage("Unexpected error occurred.");
      }
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
      <h2 className="text-xl font-semibold text-slate-900">Loan application</h2>

      <form className="mt-6 space-y-5" onSubmit={handleSubmit}>
        <div>
          <label
            htmlFor="personalCode"
            className="mb-2 block text-sm font-medium text-slate-800"
          >
            Personal code
          </label>
          <input
            id="personalCode"
            type="text"
            value={form.personalCode}
            onChange={(event) =>
              handleChange("personalCode", event.target.value)
            }
            className="w-full rounded-xl border border-slate-300 px-4 py-3 text-sm outline-none transition focus:border-slate-400 focus:ring-2 focus:ring-slate-200"
            placeholder="49002010976"
            required
          />
        </div>

        <div>
          <label
            htmlFor="loanAmount"
            className="mb-2 block text-sm font-medium text-slate-800"
          >
            Loan amount (€)
          </label>
          <input
            id="loanAmount"
            type="number"
            min={2000}
            max={10000}
            step={1}
            value={form.loanAmount}
            onChange={(event) =>
              handleChange("loanAmount", Number(event.target.value))
            }
            className="w-full rounded-xl border border-slate-300 px-4 py-3 text-sm outline-none transition focus:border-slate-400 focus:ring-2 focus:ring-slate-200"
            required
          />
        </div>

        <div>
          <label
            htmlFor="loanPeriodMonths"
            className="mb-2 block text-sm font-medium text-slate-800"
          >
            Loan period (months)
          </label>
          <input
            id="loanPeriodMonths"
            type="number"
            min={12}
            max={60}
            step={1}
            value={form.loanPeriodMonths}
            onChange={(event) =>
              handleChange("loanPeriodMonths", Number(event.target.value))
            }
            className="w-full rounded-xl border border-slate-300 px-4 py-3 text-sm outline-none transition focus:border-slate-400 focus:ring-2 focus:ring-slate-200"
            required
          />
        </div>

        {errorMessage ? (
          <div className="rounded-xl border border-rose-200 bg-rose-50 px-4 py-3 text-sm text-rose-700">
            {errorMessage}
          </div>
        ) : null}

        <button
          type="submit"
          disabled={isSubmitting}
          className="inline-flex w-full items-center justify-center rounded-xl bg-slate-900 px-4 py-3 text-sm font-medium text-white transition hover:bg-slate-800 disabled:cursor-not-allowed disabled:opacity-60"
        >
          {isSubmitting ? "Calculating..." : "Calculate decision"}
        </button>
      </form>
    </section>
  );
}
