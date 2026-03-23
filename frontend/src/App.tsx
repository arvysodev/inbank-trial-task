import { useState } from "react";
import { DecisionResultCard } from "./components/DecisionResultCard";
import { LoanDecisionForm } from "./components/LoanDecisionForm";
import type { LoanDecisionResponse } from "./types/loan";

export default function App() {
  const [result, setResult] = useState<LoanDecisionResponse | null>(null);

  return (
    <main className="min-h-screen bg-slate-50 px-6 py-12">
      <div className="mx-auto max-w-5xl">
        <header className="mb-10">
          <h1 className="text-3xl font-semibold tracking-tight text-slate-900">
            Loan Decision Calculator
          </h1>
          <p className="mt-3 max-w-2xl text-sm text-slate-600">
            Submit a loan request to calculate the maximum approved amount and
            period based on the applicant’s credit profile.
          </p>
        </header>

        <div className="grid gap-6 lg:grid-cols-2">
          <LoanDecisionForm onResult={setResult} />

          {result ? (
            <DecisionResultCard result={result} />
          ) : (
            <section className="rounded-2xl border border-dashed border-slate-300 bg-white p-6 text-sm text-slate-500 shadow-sm">
              Decision result will appear here after submitting the form.
            </section>
          )}
        </div>
      </div>
    </main>
  );
}
