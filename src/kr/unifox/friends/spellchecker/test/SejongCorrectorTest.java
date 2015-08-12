package kr.unifox.friends.spellchecker.test;

import java.io.IOException;
import java.util.List;

import kr.unifox.sejong.ling.Component;
import kr.unifox.sejong.ling.Hangeul;
import kr.unifox.sejong.ling.HangeulException;
import kr.unifox.sejong.spellchecker.Candidate;
import kr.unifox.sejong.spellchecker.CandidateArray;
import kr.unifox.sejong.spellchecker.CandidateSearcher;
import kr.unifox.sejong.spellchecker.Dictionary;
import kr.unifox.sejong.spellchecker.EvaluationResult;
import kr.unifox.sejong.spellchecker.Evaluator;
import kr.unifox.sejong.spellchecker.TextFileDictionary;
import kr.unifox.sejong.spellchecker.mistakes.AhnAhnhMisktake;
import kr.unifox.sejong.spellchecker.mistakes.JosaJongSungMistake;
import kr.unifox.sejong.spellchecker.mistakes.Mistake;
import kr.unifox.sejong.spellchecker.mistakes.Repaired;

public class SejongCorrectorTest {

	Dictionary dic;
	
	// 후보검색기 테스트해봅니다.
	private void testCandidateSearcher(String eojeol) throws HangeulException
	{
		long start = System.currentTimeMillis();
		
		CandidateSearcher cs = new CandidateSearcher(dic, eojeol, Hangeul.spreadHangeulString(eojeol));
		Evaluator eval = new Evaluator(dic);
		
		System.out.println("Start CandidateSearcher Test: " + eojeol);
		
		cs.search();

		AhnAhnhMisktake mistake = new AhnAhnhMisktake(dic);
		
		int i = 1;
		for(CandidateArray array : cs.getCandidateArrayList())
		{
			// 후보 목록
			System.out.printf("Candidates count: %d.\n", i++);
			for(Candidate candi : array)
				System.out.print(candi + " ");
			System.out.println();
			System.out.println();
			
			// 후보 개수 출력하고 평가시작
			EvaluationResult result = eval.evaluate(array);
			
			// 평가 결과
			System.out.printf("Evaluation Result: %s\n", result.evaluation);
			// 정확한 거 내용 출력
			System.out.printf("Corrects count: %d\n", result.corrects.size());
			for(Candidate candi: result.corrects)
			{
				printCandidate(candi);
				
				Repaired r = new Repaired();
				mistake.checkMistake(candi, r);
				if(r.hasMistake)
				{
					System.out.print("It has Mistake! Please Repaired to ");
					printCandidate(r.repaired);
				}
			}
			// 이상한거 내용 출력
			System.out.printf("Stranges count: %d\n", result.stranges.size());
			for(Candidate candi: result.stranges)
			{
				printCandidate(candi);
			}
			
			System.out.println();
		}
		
		long elapsed = System.currentTimeMillis() - start;
		System.out.printf("%dms elapsed\n", elapsed);
		computeUsedMemory();
		System.out.println("-------------------------------");
	}
	
	private void printCandidate(Candidate candi)
	{
		for(Component comp : candi.compList)
		{
			System.out.printf("%s(%s) ", comp.getSource(), comp.getTypeName());
		}
		System.out.println();
		if(candi.mistakes != null)
		{
			System.out.println("틀린거 있음 있음!");
			for(Mistake mis : candi.mistakes)
				System.out.printf("%s: %s\n", mis.type, mis.reason);
		}
	}
	
	public SejongCorrectorTest() {
		try {
			dic = new TextFileDictionary("db");
			computeUsedMemory();
			testCandidateSearcher("않되요");
			testCandidateSearcher("안되요");
			testCandidateSearcher("않된다");
			testCandidateSearcher("안된다");
			computeUsedMemory();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {

		new SejongCorrectorTest();
		

	}

	private static final long MEGABYTE = 1024L * 1024L;
	
	public static long bytesToMegabytes(long bytes) {
		return bytes / MEGABYTE;
	}
  	public static void computeUsedMemory()
  	{
	    Runtime runtime = Runtime.getRuntime();
	    // Run the garbage collector
	    //runtime.gc();
	    // Calculate the used memory
	    long memory = runtime.totalMemory() - runtime.freeMemory();
	    System.out.println("Used memory is bytes: " + memory);
	    System.out.println("Used memory is megabytes: "
	        + bytesToMegabytes(memory));
  	}
}
